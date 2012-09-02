var config = module.exports;

config["Browser tests"] = {
    environment: "browser",
    rootPath: "../",
    extensions: [require("buster-coffee")],
    sources: ["assets/js/jquery.js","assets/js/jquery-ui.js","assets/**/*.coffee"],
    tests: ["test/**/*-test.coffee"]
};
