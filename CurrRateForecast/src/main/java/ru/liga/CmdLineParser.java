package ru.liga;

import java.util.HashMap;
import java.util.Map;

/*
анализатор коммандной строки
*/
public class CmdLineParser {

    private String command;
    private Map<String, String> commandOptions;

    public CmdLineParser(String command)
    {
        this.command = command;
        this.commandOptions = new HashMap<String, String>();
    }

    /**
     * Разбор командной строки
     *
     * @return Валидность команды
     */
    public boolean parseAndValidate() {
        boolean retVal = false;

        String[] options = command.replaceAll("[\\s]{2,}", " ").trim().split(" ");

        //проверка допустимости команды
        if ("rate".equalsIgnoreCase(options[0])) {
            retVal = true;
            commandOptions.put("command", "rate");

            //проверка заполнения параметры валюта
            if (!"".equals(options[1])) {
                commandOptions.put("currency", options[1]);
            }//todo else лучше не переносить на след строку, } else {
            else {
                retVal = false;
            }

            //проверка заполнения параметра период прогнозирования
            if ("tomorrow".equalsIgnoreCase(options[2]) || "week".equalsIgnoreCase(options[2])){
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
        return commandOptions.get(optionName);
    }
}
