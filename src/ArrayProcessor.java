class ArrayProcessor {
    private int[][] numbers;

    public ArrayProcessor(int[][] numbers) {
        this.numbers = numbers;
    }

    public int[][] getNumbers() {
        return numbers;
    }

    public char[][] numberArrayToCharArray() {
        char[][] result = new char[numbers.length][numbers[0].length];
        int alphabetLength = 26;
        for (int i = 0; i < numbers.length; i++) {
            for (int j = 0; j < numbers[i].length; j++) {
                int num = numbers[i][j] % alphabetLength;
                if (num == 0) {
                    num = alphabetLength;
                }
                result[i][j] = (char) ('a' + num - 1);
            }
        }
        return result;
    }
}