package ru.liga;

import java.util.HashMap;
import java.util.Map;

/*
анализатор командной строки
*/
public class CmdLineParser {
    private final static String COMMAND_RATE = "rate";

    private final String command;
    private Map<CommandOption, String> options;

    public CmdLineParser(String command)
    {
        this.command = command;
        this.options = new HashMap<>();
    }

    /**
     * Разбор командной строки
     *
     * @return Валидность команды
     */
    public boolean parseAndValidate() {
        boolean retVal = false;

        String[] arrOptions = command.replaceAll("[\\s]{2,}", " ").trim().split(" ");

        //проверка допустимости команды
        if (COMMAND_RATE.equalsIgnoreCase(arrOptions[0])) {
            retVal = true;
            options.put(CommandOption.COMMAND, COMMAND_RATE);

            //проверка заполнения параметры валюта
            if (!"".equals(arrOptions[1])) {
                options.put(CommandOption.CURRENCY, arrOptions[1]);
            } else {
                retVal = false;
            }

            //проверка заполнения параметра период прогнозирования
            for (ForecastPeriod p:ForecastPeriod.values()){
                if (p.getPeriod().equalsIgnoreCase(arrOptions[2])){
                    options.put(CommandOption.PERIOD, p.name());
                    break;
                }
            }
            retVal = retVal && options.containsKey(CommandOption.PERIOD);
        }
        return retVal;
    }

    /**
     * Получение параметра команды по имени
     *
     * @param option Имя параметра команды
     * @return Значение параметра команды
     */
    public String getOptionValue(CommandOption option){
        return options.get(option);
    }
}
