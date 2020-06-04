const fs = require('fs')
const rules = {
    novorCsv: /^\d*,\s(\d*),\s[\d.]+,\s[\d.]+,\s\d*,\s[(?(\-\d.)|\d.)]+,\s[(?(\-\d.)|\d.)]+,\s[(?(\-\d.)|\d.)]+,\s[\d.]+,\s([^0-9,]*)/gm,
    csvVersion: /^\#.*v([\d.]+)/gm,
    mgf: /seq={(.*?)}|SCANS=(\d*)/gm,
}

class NovorScorerHelper {
    static readFile(path) {
        let text = fs.readFileSync(path, { encoding: 'utf-8' })
        return text
    }

    static writeFile(path, data) {
        let encodedData = JSON.stringify(data, null, 4)
        fs.writeFileSync(path, encodedData, { encoding: 'utf-8' })
    }

    static findPeptideMgf(mgfText) {
        let match = null;
        let results = []

        let tempMatch = ''
        while ((match = rules.mgf.exec(mgfText)) !== null) {
            if (match[1] != null)
                tempMatch = match[1]
            if (match[2] != null && tempMatch !== undefined)
                results.push({ [match[2]]: tempMatch })
        }
        return results
    }
    
    static findPeptideCsv(csvText) {
        let match = null;
        let results = []

        while ((match = rules.novorCsv.exec(csvText)) !== null) {
            if (match[1] != null)
                results.push({ [match[1]]: match[2] })
        }
        return results
    }

    static findNovorVersion(csvText) {
        let [[, version]] = [...csvText.matchAll(rules.csvVersion)]
        return version
    }

    static compare(mgfResults, csvResults) {
        let output = []
        mgfResults.forEach((peptide) => {
            let [scanAndSeq] = Object.entries(peptide)
            let [novorScanAndSeq] = Object.values(csvResults[scanAndSeq[0]])

            let res = {}
            res.scan = scanAndSeq[0]
            res.mgfPeptide = scanAndSeq[1]
            res.novorPeptide = novorScanAndSeq
            res.success = 0
            if (res.mgfPeptide == res.novorPeptide)
                res.success = 1
            output.push(res)
        })
        return output
    }

    static compareLatestNovor(mgfResults, csvResults) {
        let output = this.compare(mgfResults, csvResults)
        let outputWithoutParentheses = output.map(({ ...peptide }) => {
            peptide.novorPeptide = peptide.novorPeptide.replace(/\([^)]*\)/g, '')
            if (peptide.novorPeptide == peptide.mgfPeptide)
                peptide.success = 1
            else
                peptide.success = 0
            return peptide
        })

        return [output, outputWithoutParentheses]
    }

    static countMatch(output) {
        return output.filter(peptide => peptide.success == 1).length
    }

    static logCompare(params) {
        return `Used novor 1.1b version with default config.\nMatch rate is ${this.countMatch(params.matchResult)}/${params.matchResult.length}.\nYou should check '${params.path3}' for more info.`
    }

    static logCompareLastestNovor(params) {
        return `Used novor 1.05 version with default config.\nMatch rate is ${this.countMatch(params.matchResult)}/${params.matchResult.length},\nMatch rate without parentheses [(Cam), (O)] is ${this.countMatch(params.matchResultWithoutParentheses)}/${params.matchResultWithoutParentheses.length}.\nYou should check '${params.path3}' and '${params.path4}' for more info.`
    }
}

module.exports = NovorScorerHelper