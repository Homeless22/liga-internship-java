package ru.liga;

import java.util.HashMap;
import java.util.Map;

/*
анализатор коммандной строки
*/
public class CmdLineParser {
    //todo не забывай про переносы строк, чтобы разделять блоки в классе, так будет легче ччитать код
    private String command;
    private Map<String, String> commandOptions;
    public CmdLineParser(String command)
    {
        //todo здесь у тебя разный формат обращения к полям, через "this." и без
        // разницы нет, но лучше чтобы было всё одного формата
        this.command = command;
        commandOptions = new HashMap<String, String>();
    }

    /**
     * Разбор командной строки
     *
     * @return Валидность команды
     */
    //todo епонятное название метода, он же не парсит, а валидирует
    // если метод возвращает boolean, то лучше назвать его как условие, например isValid()
    public boolean parse() {
        boolean retVal = false;

        String[] options = command.replaceAll("[\\s]{2,}", " ").trim().split(" ");
        //проверка допустимости команды
        //todo idea предлагает заменить условие на "rate".equalsIgnoreCase(options[0]), думаю так выглядит лучше
        if ("rate".equals(options[0].toLowerCase())) {
            retVal = true;
            commandOptions.put("command", "rate");

            //проверка заполнения параметры валюта
            if (!"".equals(options[1])) {
                commandOptions.put("currency", options[1]);
            }
            else {
                retVal = false;
            }

            //проверка заполнения параметра период прогнозирования
            //todo тоже, что и выше
            if ("tomorrow".equals(options[2].toLowerCase()) || "week".equals(options[2].toLowerCase())){
                commandOptions.put("period", options[2].toLowerCase());
            }
            else {
                retVal = false;
            }
        }
        return retVal;
    }

    /**
     * Получение параметра команды по имени
     *
     * @param optionName Имя параметра команды
     * @return Значение параметра команды
     */
    public String getOptionValue(String optionName){
        //todo в этом методе можно оставить только строку - return commandOptions.get(optionName);
        // метод get у мапы возвращает null, если в мапе нет этого ключа
        String retVal = null;

        if (commandOptions.containsKey(optionName)) {
            retVal = commandOptions.get(optionName);
        }
        return retVal;
    }
}
