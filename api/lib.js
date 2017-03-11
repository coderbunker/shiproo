const fs = require('fs')

function loadJsonText(jsonPath) {
    return fs.readFileSync(jsonPath).toString()
}

function loadJson(jsonPath) {
    return require(jsonPath)
}

module.exports = {
    loadJsonText,
    loadJson
}