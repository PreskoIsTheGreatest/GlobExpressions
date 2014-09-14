package com;

public class Glob {

    private String pattern;
    private String patternSymbol;
    private Integer startIndex;
    private Integer endIndex;
    private Integer countOfConstantSymbolsBeforeMatcher;
    private Integer countOfConstantSymbolsAfterMatcher;
    private String constantStringBeforePattern;
    private String constantStringAfterPattern;
    
    // Constants
    final String questionMarkSymbol = "?";
    final String oneStarSymbol = "*";
    final String twoStarsSymbol = "**";
    final String directorySymbol = "/";
    final String curlyBracesSymbol = "{}";
    final String leftCurlyBrace = "{";
    final String rightCurlyBrace = "}";

    public Glob(String pattern) {
        this.pattern = pattern;
        this.patternSymbol = findPattern(this.pattern);
        this.startIndex = findStartOfPattern(this.pattern);
        this.endIndex = findEndOfPattern(this.pattern);
        this.countOfConstantSymbolsBeforeMatcher = (this.startIndex);
        this.countOfConstantSymbolsAfterMatcher = (this.pattern.length() - (this.endIndex + 1));
        this.constantStringBeforePattern = this.pattern.substring(0, this.startIndex);
        if (this.endIndex + 1 == this.pattern.length()) {
            this.constantStringAfterPattern = "";
        } else {
            this.constantStringAfterPattern = this.pattern.substring(this.endIndex + 1, this.pattern.length());
        }

    }

    static Glob compile(String pattern) {
        return new Glob(pattern);
    }

    public Boolean matches(String word) {
        Integer lenght = word.length();
        if (getPatternSymbol().equals(questionMarkSymbol)) {
            return questionMarkSymbolMatcherAction(word, lenght);
        } else if (getPatternSymbol().equals(oneStarSymbol)) {
            return oneStarSymbolMatcherAction(word, lenght);
        } else if (getPatternSymbol().equals(curlyBracesSymbol)) {
            return curlyBracesMatcherAction(word);
        } else if (getPatternSymbol().equals(twoStarsSymbol)) {
            return twoStarSymbolMatcherAction(word, lenght);
        }
        return false;
    }

    private Boolean twoStarSymbolMatcherAction(String word, Integer lenght) {
        if ((getStartIndex() + 1) > lenght || (getCountOfConstantSymbolsAfterMatcher() + 1) > lenght) {
            return false;
        }
        String symbolInTheWordBeforePattern = word.substring(0, getStartIndex());
        String symbolInTheWordAfterPattern = word.substring(lenght - getCountOfConstantSymbolsAfterMatcher(), lenght);
        Boolean isCorrectConstantsBeforePatternSymbol = symbolInTheWordBeforePattern
                .equals(getConstantStringBeforePattern());
        Boolean isCorrectConstantsAfterPatternSymbol = symbolInTheWordAfterPattern
                .equals(getConstantStringAfterPattern());
        return (isCorrectConstantsBeforePatternSymbol && isCorrectConstantsAfterPatternSymbol);
    }

    private Boolean curlyBracesMatcherAction(String word) {
        String symbolsBettwenParenthesis = getPattern().substring(getStartIndex() + 1, getEndIndex());
        String[] symbolsBettwenParenthesisArray = symbolsBettwenParenthesis.split(",");
        Boolean isFoundPattern = false;
        for (String endOfString : symbolsBettwenParenthesisArray) {
            if ((getConstantStringBeforePattern() + endOfString + getConstantStringAfterPattern()).equals(word)) {
                isFoundPattern = true;
            }
        }
        return isFoundPattern;
    }

    private Boolean oneStarSymbolMatcherAction(String word, Integer lenght) {
        if ((getStartIndex() + 1) > lenght || (getCountOfConstantSymbolsAfterMatcher() + 1) > lenght) {
            return false;
        }
        String symbolsInTheWordBeforePattern = word.substring(0, getStartIndex());
        Boolean isCorrectConstantsBeforePatternSymbol = symbolsInTheWordBeforePattern
                .equals(getConstantStringBeforePattern());
        if (isCorrectConstantsBeforePatternSymbol == false) {
            return false;
        }
        String symbolsInTheWordAfterPattern = word.substring(lenght - getCountOfConstantSymbolsAfterMatcher(), lenght);
        Boolean isCorrectConstantsAfterPatternSymbol = symbolsInTheWordAfterPattern
                .equals(getConstantStringAfterPattern());
        if (isCorrectConstantsAfterPatternSymbol == false) {
            return false;
        }
        Boolean isCrossingDirectoryBoundariesInBeforePattern = !(symbolsInTheWordBeforePattern.contains(directorySymbol));
        if (isCrossingDirectoryBoundariesInBeforePattern == false) {
            return false;
        }
        Boolean isCrossingDirectoryBoundariesInAfterPattern = !(symbolsInTheWordAfterPattern.contains(directorySymbol));
        if (isCrossingDirectoryBoundariesInAfterPattern == false) {
            return false;
        }
        String wordInPattern = word.substring(getStartIndex(), lenght - getCountOfConstantSymbolsAfterMatcher());

        Boolean isCrossingDirectoryBoundariesInWordInPatter = !(wordInPattern.contains(directorySymbol));
        if (isCrossingDirectoryBoundariesInWordInPatter == false) {
            return false;
        }
        Boolean isCrossingDirectoryBoundaries = (isCrossingDirectoryBoundariesInBeforePattern
                && isCrossingDirectoryBoundariesInAfterPattern && isCrossingDirectoryBoundariesInWordInPatter);
        return (isCorrectConstantsBeforePatternSymbol && isCorrectConstantsAfterPatternSymbol && isCrossingDirectoryBoundaries);
    }

    private Boolean questionMarkSymbolMatcherAction(String word, Integer lenght) {
        if ((getStartIndex() + 1) > lenght || (getCountOfConstantSymbolsAfterMatcher() + 1) > lenght) {
            return false;
        }
        String symbolInTheWordBeforePattern = word.substring(0, this.startIndex);
        String symbolInTheWordAfterPattern = "";
        if ((getEndIndex()) != word.length()) {
            symbolInTheWordAfterPattern = word.substring(getEndIndex() + 1, word.length());
        }
        Boolean isCorrectConstantsBeforePatternSymbol = symbolInTheWordBeforePattern
                .equals(getConstantStringBeforePattern());
        Boolean isCorrectConstantsAfterPatternSymbol = symbolInTheWordAfterPattern
                .equals(getConstantStringAfterPattern());
        if (getEndIndex() == lenght) {
            return false;
        }
        return (isCorrectConstantsBeforePatternSymbol && isCorrectConstantsAfterPatternSymbol);
    }

    private String findPattern(String matcher) {
        if (matcher.contains(questionMarkSymbol)) {
            return questionMarkSymbol;
        } else if (matcher.contains(twoStarsSymbol)) {
            return twoStarsSymbol;
        } else if (matcher.contains(oneStarSymbol)) {
            return oneStarSymbol;
        } else if (matcher.contains(leftCurlyBrace) && matcher.contains(rightCurlyBrace)) {
            return curlyBracesSymbol;
        }
        return "";
    }

    private Integer findStartOfPattern(String pattern) {
        return pattern.indexOf(getPatternSymbol().charAt(0));
    }

    private Integer findEndOfPattern(String pattern) {
        if (getPatternSymbol().equals(twoStarsSymbol)) {
            return pattern.indexOf(getPatternSymbol().charAt(getPatternSymbol().length() - 1)) + 1;
        }
        return pattern.indexOf(getPatternSymbol().charAt(getPatternSymbol().length() - 1));
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getMatcher() {
        return patternSymbol;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public Integer getEndIndex() {
        return endIndex;
    }

    public Integer getCountOfConstantSymbolsBeforeMatcher() {
        return countOfConstantSymbolsBeforeMatcher;
    }

    public Integer getCountOfConstantSymbolsAfterMatcher() {
        return countOfConstantSymbolsAfterMatcher;
    }

    public String getPatternSymbol() {
        return patternSymbol;
    }

    public String getConstantStringBeforePattern() {
        return constantStringBeforePattern;
    }

    public String getConstantStringAfterPattern() {
        return constantStringAfterPattern;
    }

}
