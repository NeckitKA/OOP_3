class SentenceProcessor {
    private String sentence;

    public SentenceProcessor(String sentence) {
        this.sentence = sentence;
    }

    public  String getSentence() {
        return sentence;
    }

    public int countOneLetterWords() {
        String[] words = sentence.split(" ");
        int count = 0;
        for (String word : words) {
            if (word.length() == 1) {
                count++;
            }
        }
        return count;
    }

    public String removeOneLetterWords() {
        String[] words = sentence.split(" ");
        String result = "";
        for (String word : words) {
            if (word.length() != 1) {
                result += word + " ";
            }
        }
        return result.trim();
    }

    public String findLongestWord() {
        String[] words = sentence.split(" ");
        String longestWord = "";
        for (String word : words) {
            if (word.length() > longestWord.length()) {
                longestWord = word;
            }
        }
        return longestWord;
    }
}