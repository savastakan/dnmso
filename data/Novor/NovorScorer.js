const NovorScorerHelper = require('./NovorScorerHelper')

class NovorScorer {
    static openFiles(params) {
        let mgfText = NovorScorerHelper.readFile(params.path)
        let csvText = NovorScorerHelper.readFile(params.path2)
        delete params.path
        delete params.path2
        params.mgfText = mgfText
        params.csvText = csvText
        return params
    }

    static findPeptideInMgf(params) {
        let mgfResults = NovorScorerHelper.findPeptideMgf(params.mgfText)
        delete params.mgfText
        params.mgfResults = mgfResults
        return params
    }

    static findPeptideInCsv(params) {
        let csvResults = NovorScorerHelper.findPeptideCsv(params.csvText)
        params.csvResults = csvResults
        return params
    }

    static findNovorVersionInCsv(params) {
        let csvVersion = NovorScorerHelper.findNovorVersion(params.csvText)
        params.csvVersion = csvVersion
        delete params.csvText
        return params
    }

    static match(params) {
        if (params.csvVersion > "1.05.0573") {
            let matchResult = NovorScorerHelper.compareLatestNovor(params.mgfResults, params.csvResults)
            let matchResultWithoutParentheses = matchResult[1]
            params.matchResult = matchResult[0]
            params.matchResultWithoutParentheses = matchResultWithoutParentheses

            let outputPath = params.path3
            params.path4 = outputPath.substr(0, (outputPath.lastIndexOf('/') + 1)) + 'wo_parentheses_' + outputPath.substr((outputPath.lastIndexOf('/') + 1), outputPath.length)
            params.log = NovorScorerHelper.logCompareLastestNovor(params)
        } else {
            let matchResult = NovorScorerHelper.compare(params.mgfResults, params.csvResults)
            params.matchResult = matchResult

            params.log = NovorScorerHelper.logCompare(params)
        }

        delete params.mgfResults
        delete params.csvResults
        delete params.csvVersion
        return params
    }

    static log(params) {
        console.log(params.log)
        return params
    }

    static saveFiles(params) {
        NovorScorerHelper.writeFile(params.path3, params.matchResult)
        if (params.matchResultWithoutParentheses)
            NovorScorerHelper.writeFile(params.path4, params.matchResultWithoutParentheses)
        return params
    }
}

module.exports = NovorScorer