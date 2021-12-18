var reCacheOnceParam = (function () {
    var cookieArray = document.cookie.split(";");

    // Now take key value pair out of this array
    for (var i = 0; i < cookieArray.length; i++) {
        name = cookieArray[i].split("=")[0];
        value = cookieArray[i].split("=")[1];
        if (name === "JSESSIONID") {
            return value;
        }
    }
    return 0;
})();

requirejs.config({
    "baseUrl": ROOT_RELATIVE_PATH,
    urlArgs: "bust=" + reCacheOnceParam,
    waitSeconds: 10,
    paths: {
        jquery: "lib/jquery/jquery-1.10.2",
        jquery_ui: "lib/jquery/jquery-ui-1.11.2",
        jquery_tree: "lib/jstree/jstree-3.3.2",
        autosize: "lib/jquery/autosize-3.0.14",
        Handlebars: "lib/handlebars/handlebars-v2.0.0",

        jquery_migrate: "lib/jquery/jquery-migrate-1.2.1",
        jsSha: "lib/jsSHA-1.5.0",
        jqw: "lib/jqw"
       
    },
    shim: {
        "jquery_ui": {
            exports: "$",
            deps: ["jquery"]
        },
        "jquery_migrate": {
            exports: "$",
            deps: ["jquery"]
        },
        "history": {
            exports: "History",
            deps: ["jquery"]
        }
    }
});