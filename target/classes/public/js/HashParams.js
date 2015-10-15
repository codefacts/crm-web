function HashParams() {
    var handlers = [];

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
    return rs;
}