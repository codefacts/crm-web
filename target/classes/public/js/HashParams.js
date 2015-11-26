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
        setHash($.param(params || {}));
    }

    function getParams(hash) {
        hash = hash || getHash();
        var _params = {};
        if(hash.lastIndexOf("#") >= 0) {
            hash = hash.substr(hash.lastIndexOf("#") + 1);
        }
        if(hash.lastIndexOf("?") >= 0) {
            hash = hash.substr(hash.lastIndexOf("?") + 1);
        }
        hash = decodeURIComponent(hash || "").trim();
        if(hash == "") return _params;
        var splits = hash.split("&");
        for(var x in splits) {
            var part = splits[x] || "";
            var keyValuPair = part.split("=");
            if(keyValuPair.length > 0) {
                _params[keyValuPair[0]] = keyValuPair[1];
            }
        }
        return _params;
    }

    function on(uri, handler) {
        var parts = uri.split("/");
        var template = {originString: uri, parts: [], handler: handler};

        for(var x in parts) {
            var part = parts[x];
            if(part.trim() == "") continue;
            var p = {};
            if(stringStartsWith(part, ":")) {
                p.isVariable = true;
                p.value = part.slice(1, part.length);
            } else {
                p.isVariable = false;
                p.value = part;
            }
            template.parts.push(p);
        }
        routs.push(template);
    }

    function stringStartsWith (string, prefix) {
        return string.slice(0, prefix.length) == prefix;
    }

    var rs = {

        addHandler: function(_onChangeHandler) {
            if(!_onChangeHandler) return;
            handlers.push(_onChangeHandler);
        },
        removeHandler: function(_onChangeHandler) {
            if(!_onChangeHandler) return;
            delete handlers[handlers.indexOf(_onChangeHandler)];
            var hndls = [];
            for(var x in handlers) {
                if(!!handlers[x]) {
                    hndls.push(handlers[x]);
                }
            }
            handlers = hndls;
        },
        addAll: function(params) {
            if(!params) return;
            var pms = getParams();
            for(var x in params) {
                pms[x] = params[x]
            }
            setParams(pms);
        },
        removeAll: function(params) {
            if(!params) return;
            var pms = getParams();
            for(var x in params) {
                delete pms[x];
            }
            setParams(pms);
        },
        add(key, value) {
            var p = {};
            p[key] = value;
            rs.addAll(p);
        },
        remove: function(key) {
            var p = {};
            p[key] = null;
            rs.removeAll(p);
        },
        addParts: function(parts) {
            if(!parts) return;
            rs.addAll(rs.getParams(parts));
        },
        removeParts: function(parts) {
            if(!parts) return;
            rs.removeAll(rs.getParams(parts));
        },
        clear: function() {
            setHash("");
        },
        setParams: setParams,
        getParams: getParams,
        getHash: getHash,
        setHash: setHash,

    };

    $(window).on('hashchange', function() {
      for(var x in handlers) {
        try {
            handlers[x](rs);
        } catch (e) {
        }
      }
    });

    rs.addHandler(function(hash) {
        var hs = hash.getHash();
        var idx = hs.indexOf("?");
        hs = hs.length <= 1 ? hs : hs.slice(1, (idx < 0 ? hs.length : idx));
        console.log(hs);
        var parts = hs.split("/").map(function (part) {
            return decodeURIComponent(part);
        });
        console.log(parts);

        var maxScore = -1;
        var maxIndex = -1;
        var indx = 0;
        var templates = routs.filter(function (template) {
            indx++;
            return template.parts.length === parts.length;
        }).map(function (template) {
            template.score = 0;
            for(var x in parts) {
                if (template.parts[x].isVariable === false && template.parts[x].value === parts[x]) {
                    var n = (parts.length - x);
                    template.score += ((n * n * n) + 3 * (n * n) + 2 * n) / 6;
                    if(template.score > maxScore) {
                        maxScore = template.score;
                        maxIndex = indx;
                    }
                }
            }
            return template;
        });

        var template = routs[maxIndex] || templates[0];

        if(!!template) {
            template.handler(rs);
        }

        console.log(templates);
    });

    on("1/as/:2/3/:5/7", function() {
        console.log("1/as/:2/3/:5/7");
    });
    on("1/as/:2/3/:5", function () {
        console.log("1/as/:2/3/:5/7");
    });
    on("1/as/:2/:5/7", function () {
        console.log("1/as/:2/3/:5/7");
    });
    on("a/3/:5/7", function () {
        console.log("a/3/:5/7");
    });
    return rs;
}

window.HashParams = HashParams();