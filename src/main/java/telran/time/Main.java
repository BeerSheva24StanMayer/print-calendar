package telran.time;

import java.rmi.StubNotFoundException;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

record MonthYear(int month, int year) {
}

public class Main {
    public static void main(String[] args) {
        try {
            MonthYear monthYear = getMonthYear(args); // if no arguments current year and month should be applied
            int firstDayOfWeek = ifFirstDayOfWeek(args);
            printCalendar(monthYear, firstDayOfWeek);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private static void printCalendar(MonthYear monthYear, int firstDayOfWeek) {
        printTitle(monthYear);
        printWeekDays(firstDayOfWeek);
        printDates(monthYear, firstDayOfWeek);
    }

    private static void printTitle(MonthYear monthYear) {
        String month = Month.of(monthYear.month()).toString();
        int year = monthYear.year();
        String text = year + " " + " " + month;
        printCentered(text);
    }

    private static void printWeekDays(int firstDayOfWeek) {
        DayOfWeek[] daysOfweek = DayOfWeek.values();
        String text = getDaysLine(daysOfweek, firstDayOfWeek);
        printCentered(text);
    }


    private static void printDates(MonthYear monthYear, int firstDayOfWeek) {
        int offset = getOffset(getFirstDayofWeek(monthYear), firstDayOfWeek);
        int lastDayofMonth = getLastDayOfMonth(monthYear);
        if (offset < 0) {
            offset = 7 + offset;
        }
        printOffset(offset);
        printDays(lastDayofMonth, offset);
    }

    private static void printDays(int lastDayofMonth, int offset) {
        int day = 1;
        while(day <= lastDayofMonth) {
            System.out.printf("%4d", day);
            day++;
            offset++;
            if(offset % 7 == 0) {
                System.out.println();
            }
        }
    }

    private static void printOffset(int offset) {
        
        for(int i = 1; i <= offset; i++) {
            System.out.printf("%4s", " ");
        }
    }

    private static MonthYear getMonthYear(String[] args) throws Exception {
        LocalDate lcd = LocalDate.now();
        int month = lcd.getMonthValue();
        int year = lcd.getYear();
        if (args.length > 1) {
            try {
                month = ifMonth(args[0]);
                year = ifYear(args[1]);
            } catch (DateTimeException e) {
                System.out.println("Month should be between 1 and 12, current date calendar is printed");
            } catch (NumberFormatException e) {
                System.out.println("Month or/and Year must be numbers, current date calendar is printed");
            }
        }
        return new MonthYear(month, year);
    }

    private static int ifYear(String string) {
        int year = Integer.parseInt(string);
        return year;
    }

    private static int ifMonth(String string) throws Exception {
        int month = Integer.parseInt(string);
        if (month < 1 || month > 12) {
            throw new DateTimeException("Wrong Month Number");
        }
        return month;
    }

    private static int ifFirstDayOfWeek(String[] args) {
        int day = 1;
        if (args.length == 3) {
            try {
            day = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            
        }
        if(day < 1 || day > 7) {
            System.out.println("Wrong day, must be between 1 an 7, Monday set as first day of week");
        }
        }
        return day;
    }

    private static int getFirstDayofWeek(MonthYear monthYear) {
        return LocalDate.of(monthYear.year(), monthYear.month(), 1).getDayOfWeek().getValue();
    }

    private static int getOffset(int firstWeekDay, int firstDayOfWeek) {
        // TODO
        // shift on this offset for starting printin
        return firstWeekDay - firstDayOfWeek;
    }


    private static int getLastDayOfMonth(MonthYear monthYear) {
        // TODO
        return LocalDate.of(monthYear.year(), monthYear.month(), 1).lengthOfMonth();
    }

    private static String getDaysLine(DayOfWeek[] daysOfweek, int firstDayOfWeek) {
        DayOfWeek[] res = java.util.Arrays.copyOf(daysOfweek, daysOfweek.length);
        System.arraycopy(daysOfweek, firstDayOfWeek - 1, res, 0, 8 - firstDayOfWeek);
        System.arraycopy(daysOfweek, 0, res, 8 - firstDayOfWeek , firstDayOfWeek - 1);
        
        String text = "";
        for (DayOfWeek dayOfWeek : res) {
            String day = dayOfWeek.toString().substring(0, 3);
            if (dayOfWeek == res[6]) {
                text = text + day;
            }
            else {
                text = text + day + " ";
            }      
        }
        return text;
    }

    private static void printCentered(String text) {
        int totalWidth = 30;
        int padding = (totalWidth - text.length()) / 2;
        String centeredText = String.format("%" + padding + "s%s%" + padding + "s", "", text, "");
        System.out.println(centeredText);
    }
}