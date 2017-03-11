const fs = require('fs')
const path = require('path')

function loadJsonText(jsonPath) {
    return fs.readFileSync(jsonPath).toString()
}

function loadJson(jsonPath) {
    return require(jsonPath)
}

function loadQuery(name, values) {
    const query = loadJsonText(path.join(__dirname, 'fixtures', `${name}.json`))
    return JSON.parse(query.replace('$USERNAME', values.username))
}

function loadQueryText(name, values) {
    return JSON.stringify(loadQuery(name, values))
}

module.exports = {
    loadJsonText,
    loadJson,
    loadQuery,
    loadQueryText
}