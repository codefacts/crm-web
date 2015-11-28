function HashParams() {
    var handlers = [];
    var routs = [];

    function setHash(hash) {
        location.hash = hash;
    }

    function getHash() {
        return location.hash || "";
    }

    function setParams(params) {
        var hs = getHash();
        var idx = hs.indexOf("?");
        hs = idx < 0 ? hs : hs.slice(0, idx);
        setHash(hs + "?" + $.param(params || {}));
    }

    function getParams(hash) {
        hash = hash || getHash();
        var _params = {};
        if (hash.lastIndexOf("#") >= 0) {
            hash = hash.substr(hash.lastIndexOf("#") + 1);
        }
        if (hash.lastIndexOf("?") >= 0) {
            hash = hash.substr(hash.lastIndexOf("?") + 1);
        } else return _params;

        if (hash == "") return _params;
        var splits = hash.split("&");
        for (var x in splits) {
            var part = splits[x] || "";
            var keyValuPair = part.split("=", 2);
            if (keyValuPair.length > 0) {
                _params[keyValuPair[0]] = keyValuPair[1];
            }
        }
        return _params;
    }

    function on(uri, handler) {
		var $this = this;
        var parts = uri.split("/");
        var template = {originString: uri, parts: [], handler: handler};

        for (var x in parts) {
            var part = parts[x];
            if (part.trim() == "") continue;
            var p = {};
            if (part.startsWith(":")) {
                p.isVariable = true;
                p.value = part.slice(1, part.length);
            } else {
                p.isVariable = false;
                p.value = part;
            }
            template.parts.push(p);
        }
        routs.push(template);
		console.log($this);
		console.log(routs);
		return $this;
    }

    function triggerHashChange() {
        $(window).trigger('hashchange');
    }

    var rs = {

        start: function (onStartCallback) {
            try {
                onStartCallback(rs);
            } catch (e) {
                console.log(e);
            }
            triggerHashChange();
        },

        path: function () {
            var hs = getHash();
            var idx = hs.indexOf("#");
            var s = hs.slice(idx < 0 ? 0 : (idx + 1), hs.indexOf("?"));
            return s == "" ? "/" : s;
        },

        on: on,

        goto: function (uri, params) {
            uri = uri.startsWith("/") ? uri : "/" + uri;
            uri = uri + "?" + $.param(params || {});
            setHash(uri.endsWith("?") ? uri.slice(0, uri.length - 1) : uri);
        },
        reload: function () {
            triggerHashChange();
        },

        addHandler: function (_onChangeHandler) {
            if (!_onChangeHandler) return;
            handlers.push(_onChangeHandler);
        },
        removeHandler: function (_onChangeHandler) {
            if (!_onChangeHandler) return;
            delete handlers[handlers.indexOf(_onChangeHandler)];
            var hndls = [];
            for (var x in handlers) {
                if (!!handlers[x]) {
                    hndls.push(handlers[x]);
                }
            }
            handlers = hndls;
        },
        addAll: function (params) {
            if (!params) return;
            var pms = getParams();
            for (var x in params) {
                pms[x] = params[x]
            }
            setParams(pms);
        },
        removeAll: function (params) {
            if (!params) return;
            var pms = getParams();
            if (params.constructor === Array) {
                for (var x in params) {
                    delete pms[params[x]];
                }
            } else {
                for (var x in params) {
                    delete pms[x];
                }
            }
            setParams(pms);
        },
        add(key, value) {
            var p = {};
            p[key] = value;
            rs.addAll(p);
        },
        remove: function (key) {
            var p = {};
            p[key] = null;
            rs.removeAll(p);
        },
        clear: function () {
            rs.params({});
        },
        params: function (params) {
            if (!!params) {
                setParams(params);
            } else {
                return getParams();
            }
        },
        hash: function (hash) {
            if (!!hash) {
                setHash(hash);
            } else {
                return getHash(hash);
            }
        },
		getHash: getHash,
		setHash: setHash,
		getParams: getParams,
		setParams: setParams
    };

    $(window).on('hashchange', function () {
        for (var x in handlers) {
            try {
                handlers[x](rs);
            } catch (e) {
				console.log(e);
            }
        }
    });

    rs.addHandler(function (hash) {
		console.log("SEE IT");
        var hs = hash.getHash();
        var idx = hs.indexOf("?");
        hs = hs.length <= 1 ? hs : hs.slice(1, (idx < 0 ? hs.length : idx));

        var parts = hs.split("/").filter(function (part) {
            return !!part;
        }).map(function (part) {
            return decodeURIComponent(part);
        });

        var maxScore = -1;
        var maxIndex = -1;
        var indx = 0;
        var templates = routs.filter(function (template) {
            return (template.parts.length == parts.length);
        }).filter(function (template) {
            for (var x in parts) {
                if ((template.parts[x].isVariable == false) && (template.parts[x].value != parts[x])) {
                    return false;
                }
            }
            return true;
        }).map(function (template) {
            template.score = 0;
            for (var x in parts) {
                if (template.parts[x].isVariable == false && template.parts[x].value == parts[x]) {
                    var n = (parts.length - x);
                    template.score += ((n * n * n) + 3 * (n * n) + 2 * n) / 6;
                    if (template.score > maxScore) {
                        maxScore = template.score;
                        maxIndex = indx;
                    }
                }
            }
            indx++;
            return template;
        }).filter(function (template) {
            return (template.score >= maxScore);
        }).forEach(function (template) {
            try {
                var params = {};
                for (var x in template.parts) {
                    var part = template.parts[x];
                    if (part.isVariable) {
                        params[part.value] = parts[x];
                    }
                }
                template.handler(hash, params);
            } catch (e) {
                console.warn(e);
            }
        });
    });

    return rs;
}