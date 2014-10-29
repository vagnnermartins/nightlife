package br.com.metasix.olhos_do_rio.componentebox.lib.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DataUtil {
    public static String transformDateToSting(Date date, String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static Date transformStringToDate(String format, String date) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(date);
    }

    public static String obterDataPorExetenso(Date date) throws ParseException{
        StringBuilder sb = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        sb.append(obterDiaDaSemana(calendar.get(Calendar.DAY_OF_WEEK)));
        sb.append(", ");
        sb.append(calendar.get(Calendar.DAY_OF_MONTH));
        sb.append(" de ");
        sb.append(obterMes(calendar.get(Calendar.MONTH)));
        sb.append(" ");
        sb.append(calendar.get(Calendar.YEAR));
        return sb.toString();
    }

    public static double diferencaEmDias(Date dataInicial, Date dataFinal){
        double result = 0;
        long diferenca = dataFinal.getTime() - dataInicial.getTime();
        double diferencaEmDias = (diferenca /1000) / 60 / 60 /24;
        long horasRestantes = (diferenca /1000) / 60 / 60 %24;
        result = diferencaEmDias + (horasRestantes /24d);

        return result;
    }

    public static int getHourOrMinuteOrSecond(int wich, String date){
        Date data;
        int retorno = 0;
        try {
            data = transformStringToDate("HH:mm", date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(data);
            switch (wich) {
                case Calendar.HOUR_OF_DAY:
                    return calendar.get(Calendar.HOUR_OF_DAY);
                case Calendar.MINUTE:
                    return calendar.get(Calendar.MINUTE);
                case Calendar.SECOND:
                    return calendar.get(Calendar.SECOND);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return retorno;
    }

    public static String obterHoraMinutoComDoisDigitos(int hourOrMinute){
        String retorno = String.valueOf(hourOrMinute);
        if(retorno.length() == 1){
            retorno = "0" + hourOrMinute;
        }
        return retorno;
    }


    private static String obterDiaDaSemana(int dia){
        String retorno;
        switch (dia) {
            case Calendar.SUNDAY:
                retorno = "Domingo";
                break;
            case Calendar.MONDAY:
                retorno = "Segunda-feira";
                break;
            case Calendar.TUESDAY:
                retorno = "Terça-feira";
                break;
            case Calendar.WEDNESDAY:
                retorno = "Quarta-feira";
                break;
            case Calendar.THURSDAY:
                retorno = "Quinta-feira";
                break;
            case Calendar.FRIDAY:
                retorno = "Sexta-feira";
                break;
            default:
                retorno = "Sábado";
                break;
        }
        return retorno;
    }

    private static String obterMes(int mes){
        String retorno = null;
        switch (mes) {
            case Calendar.JANUARY:
                retorno = "Janeiro";
                break;
            case Calendar.FEBRUARY:
                retorno = "Fevereiro";
                break;
            case Calendar.MARCH:
                retorno = "Março";
                break;
            case Calendar.APRIL:
                retorno = "Abril";
                break;
            case Calendar.MAY:
                retorno = "Maio";
                break;
            case Calendar.JUNE:
                retorno = "Junho";
                break;
            case Calendar.JULY:
                retorno = "Julho";
                break;
            case Calendar.AUGUST:
                retorno = "Agosto";
                break;
            case Calendar.SEPTEMBER:
                retorno = "Setembro";
                break;
            case Calendar.OCTOBER:
                retorno = "Outubro";
                break;
            case Calendar.NOVEMBER:
                retorno = "Novembro";
                break;
            case Calendar.DECEMBER:
                retorno = "Dezembro";
                break;
        }
        return retorno;
    }
}
