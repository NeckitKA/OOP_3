import java.io.*;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

class Menu {
    private static Scanner scanner = new Scanner(System.in);

    private static void displayMainMenu() {
        System.out.println("Выберите действие:");
        System.out.println("1. Работа со строкой");
        System.out.println("2. Работа с массивом");
        System.out.println("3. Выход");
    }
    public static void handleMainChoice() {
        while (true) {
            displayMainMenu();
            int choice = getValidIntegerInput();
            scanner.nextLine();

            switch (choice) {
                case 1 -> handleStringOperations();
                case 2 -> handleArrayOperations();
                case 3 -> {
                    System.out.println("Выход из программы.");
                    return;
                }
                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }
    private static int getValidIntegerInput() {
        while (!scanner.hasNextInt()) {
            System.out.println("Ошибка ввода! Пожалуйста, введите целое число.");
            scanner.next();
        }
        return scanner.nextInt();
    }
    private static String getFileNameInput(boolean createMode) {
        while (true) {
            try {
                System.out.print("Оставьте строку пустой для выхода.\n");
                System.out.print("Введите имя файла" + (createMode ? " (расширение необязательно): " : " (с расширением): "));
                String fileName = scanner.nextLine().trim();

                if (fileName.isEmpty()) {
                    System.out.println("Выход из меню.");
                    return null;
                }
                if (fileName.matches(".*[\\\\/*:?\"<>|].*")) {
                    throw new IllegalArgumentException("Имя файла содержит недопустимые символы: \\ / * : ? < > |");
                }

                if (createMode) {
                    if (!fileName.contains(".")) {
                        fileName += ".txt";
                    }
                    return fileName;
                } else {
                    if (!fileName.contains(".")) {
                        throw new IllegalArgumentException("Имя файла должно содержать расширение!");
                    }

                    File file = new File(fileName);
                    if (!file.exists()) {
                        throw new FileNotFoundException("Файл не найден! Введите заново или оставьте строку пустой для выхода.");
                    }
                    return fileName;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
            } catch (FileNotFoundException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }
    private static void handleFileSaving(Object processor) {
        System.out.println("1. Сохранить в текстовый файл");
        System.out.println("2. Выход");

        int choice = getValidIntegerInput();
        scanner.nextLine();

        switch (choice) {
            case 1 -> {
                if (processor instanceof SentenceProcessor sentenceProcessor) {
                    saveToFileForSentence(sentenceProcessor);
                } else if (processor instanceof ArrayProcessor arrayProcessor) {
                    saveToFileForArray(arrayProcessor);
                }
            }
            case 2 -> System.out.println("Выход.");
            default -> System.out.println("Неверный выбор. Попробуйте снова.");
        }
    }
    private static void displaySentence(SentenceProcessor processor) {
        System.out.println("Оригинальная строка: " + processor.getSentence());
    }
    private static void displaySentenceResults(SentenceProcessor processor) {
        System.out.println("Число однобуквенных слов: " + processor.countOneLetterWords());
        System.out.println("Предложение без однобуквенных слов: " + processor.removeOneLetterWords());
        System.out.println("Самое длинное слово: " + processor.findLongestWord());
    }
    private static void displayArray(ArrayProcessor processor) {
        System.out.println("Оригинальный массив:");
        int[][] numbers = processor.getNumbers();
        for (int i = 0; i < numbers.length; i++) {
            for (int j = 0; j < numbers[i].length; j++) {
                System.out.print(numbers[i][j] + " ");
            }
            System.out.println();
        }
    }
    private static void displayArrayResults(ArrayProcessor processor) {
        System.out.println("Преобразованный массив:");
        try {
            char[][] charArray = processor.numberArrayToCharArray();

            if (charArray == null || charArray.length == 0) {
                System.out.println("Массив пуст или не был преобразован.");
                return;
            }

            for (char[] row : charArray) {
                for (char ch : row) {
                    System.out.print(ch + " ");
                }
                System.out.println();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Ошибка при обработке массива: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Массив не был инициализирован.");
        }
    }
    private static int[][] inputArray() {
        int rows = 0;
        int cols = 0;
        while ( rows<=0 || cols<=0) {
            System.out.print("Введите количество строк: ");
            rows = getValidIntegerInput();
            System.out.print("Введите количество столбцов: ");
            cols = getValidIntegerInput();
            if (rows<=0 || cols<=0) {
                System.out.print("Введите целые числа больше 0\n");
            }
        }
        int[][] array = new int[rows][cols];

        System.out.println("Введите элементы массива:");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print("Элемент [" + i + "][" + j + "]: ");
                array[i][j] = getValidIntegerInput();
            }
        }
        return array;
    }
    private static void handleStringOperations() {
        System.out.println("Выберите источник данных:");
        System.out.println("1. Ввести данные с консоли");
        System.out.println("2. Загрузить данные из файла");
        System.out.println("3. Выход");

        int choice = getValidIntegerInput();
        scanner.nextLine();

        switch (choice) {
            case 1 -> {
                System.out.print("Введите строку: ");
                String sentence = scanner.nextLine();
                SentenceProcessor sentenceProcessor = new SentenceProcessor(sentence);
                displaySentence(sentenceProcessor);
                displaySentenceResults(sentenceProcessor);
                handleFileSaving(sentenceProcessor);
            }
            case 2 -> {
                String fileName = getFileNameInput(false);
                if (fileName==null) {
                    return;
                }
                try {
                    String content = readFile(fileName);
                    SentenceProcessor sentenceProcessor = new SentenceProcessor(content);
                    displaySentence(sentenceProcessor);
                    displaySentenceResults(sentenceProcessor);
                    handleFileSaving(sentenceProcessor);
                } catch (IOException e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
            case 3 -> System.out.println("Возврат в главное меню.");
            default -> System.out.println("Неверный выбор. Попробуйте снова.");
        }
    }
    private static String readFile(String fileName) throws IOException {
        String content = new String();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content+=line+" ";
            }
        }
        return content.trim();
    }
    private static void handleArrayOperations() {
        System.out.println("Выберите источник данных:");
        System.out.println("1. Ввести данные с консоли");
        System.out.println("2. Загрузить данные из файла");
        System.out.println("3. Выход");

        int choice = getValidIntegerInput();
        scanner.nextLine();

        switch (choice) {
            case 1 -> {
                int[][] numbers = inputArray();
                ArrayProcessor arrayProcessor = new ArrayProcessor(numbers);
                displayArray(arrayProcessor);
                displayArrayResults(arrayProcessor);
                handleFileSaving(arrayProcessor);
            }
            case 2 -> {
                String fileName = getFileNameInput(false);
                if (fileName==null) {
                    return;
                }
                int[][] numbers = readArrayFromFile(fileName);
                ArrayProcessor arrayProcessor = new ArrayProcessor(numbers);
                displayArray(arrayProcessor);
                displayArrayResults(arrayProcessor);
                handleFileSaving(arrayProcessor);
            }
            case 3 -> System.out.println("Возврат в главное меню.");
            default -> System.out.println("Неверный выбор. Попробуйте снова.");
        }
    }
    private static int[][] readArrayFromFile(String fileName) {
        List<int[]> dynamicArray = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String[] numbers = line.split(" ");
                boolean isValid = true;
                int[] row = new int[numbers.length];

                for (int j = 0; j < numbers.length; j++) {
                    try {
                        row[j] = Integer.parseInt(numbers[j]);
                    } catch (NumberFormatException e) {
                        System.out.printf("Ошибка в данных на строке %d: '%s'. Пропуск строки.%n", lineNumber, line);
                        isValid = false;
                        break;
                    }
                }

                if (isValid) {
                    dynamicArray.add(row);
                }
            }
        } catch (IOException e) {
            System.out.printf("Ошибка при чтении файла '%s'. Проверьте путь и содержимое.%n", fileName);
            return new int[0][0];
        }

        int[][] array = new int[dynamicArray.size()][];
        for (int i = 0; i < dynamicArray.size(); i++) {
            array[i] = dynamicArray.get(i);
        }
        return array;
    }
    private static void saveToFileForSentence(SentenceProcessor processor) {
        String fileName = getFileNameInput(true);
        if (fileName==null) {
            return;
        }
        String originalFileName = getOriginalFileName(fileName); // Получаем имя для оригинального файла

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write("Оригинальная строка: " + processor.getSentence() + "\n");
            writer.write("Число однобуквенных слов: " + processor.countOneLetterWords() + "\n");
            writer.write("Предложение без однобуквенных слов: " + processor.removeOneLetterWords() + "\n");
            writer.write("Самое длинное слово: " + processor.findLongestWord() + "\n");
            writer.write("--------------------------------------------------------\n");
            System.out.println("Строка сохранена в файл: " + fileName);

            // Сохранение оригинальных данных в отдельный файл
            try (BufferedWriter originalWriter = new BufferedWriter(new FileWriter(originalFileName))) {
                originalWriter.write(processor.getSentence() + "\n");
                System.out.println("Оригинальная строка сохранена в файл: " + originalFileName);
            }

        } catch (IOException e) {
            System.out.println("Ошибка при сохранении файла: " + e.getMessage());
        }
    }

    private static void saveToFileForArray(ArrayProcessor processor) {
        String fileName = getFileNameInput(true);
        if (fileName==null) {
            return;
        }
        String originalFileName = getOriginalFileName(fileName); // Получаем имя для оригинального файла

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write("Оригинальный массив:\n");

            int[][] numbers = processor.getNumbers();
            if (numbers == null || numbers.length == 0) {
                writer.write("Массив чисел пуст или не задан.\n");
            } else {
                for (int[] row : numbers) {
                    for (int num : row) {
                        writer.write(num + " ");
                    }
                    writer.newLine();
                }
            }

            try {
                char[][] symbols = processor.numberArrayToCharArray();
                writer.write("Преобразованный массив:\n");
                if (symbols == null || symbols.length == 0) {
                    writer.write("Преобразованный массив пуст или не задан.\n");
                } else {
                    for (char[] row : symbols) {
                        for (char ch : row) {
                            writer.write(ch + " ");
                        }
                        writer.newLine();
                    }
                }
                writer.write("--------------------------------------------------------\n");
                System.out.println("Массив сохранен в файл: " + fileName);
                try (BufferedWriter originalWriter = new BufferedWriter(new FileWriter(originalFileName))) {
                    if (numbers == null || numbers.length == 0) {
                        originalWriter.write("Массив чисел пуст или не задан.\n");
                    } else {
                        for (int[] row : numbers) {
                            for (int num : row) {
                                originalWriter.write(num + " ");
                            }
                            originalWriter.newLine();
                        }
                    }
                    System.out.println("Оригинальный массив сохранен в файл: " + originalFileName);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Ошибка преобразования массива: " + e.getMessage());
                writer.write("Ошибка преобразования массива.\n");
                writer.write("--------------------------------------------------------\n");
            }
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении файла: " + e.getMessage());
        }
    }
    private static String getOriginalFileName(String originalFileName) {
        int dotIndex = originalFileName.lastIndexOf(".");
        if (dotIndex == -1) {
            return originalFileName + "_original";
        }
        return originalFileName.substring(0, dotIndex) + "_original" + originalFileName.substring(dotIndex);
    }
}

