package net.zdsoft.exammanage.data.utils;

import net.zdsoft.framework.utils.SortUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class Coloring {

    public static void main(String[] args) {
        List<Integer> seats = Arrays.asList(2, 3, 3, 2); // 座位信息
        List<Group> studentGroups = new ArrayList<>();
        studentGroups.add(new Group("a班", 1));
        studentGroups.add(new Group("b班", 1));
        studentGroups.add(new Group("a班", 2));
        studentGroups.add(new Group("b班", 2));
        studentGroups.add(new Group("a班", 3));
        studentGroups.add(new Group("b班", 3));
        studentGroups.add(new Group("a班", 4));
        studentGroups.add(new Group("b班", 4));
        studentGroups.add(new Group("a班", 5));
        studentGroups.add(new Group("b班", 5));
        List<Seat> seatlist = createSeats(seats, studentGroups);
//		for (Seat seat : seatlist) {
//			System.out.println(seat.getGroupId()+"*******"+seat.getIndex() + "*******("+seat.getPosX() + "," + seat.getPosY()+")");
//		}
    }

    public static List<Seat> createSeats(List<Integer> seats, List<Group> studentGroups) {
        List<Seat> seatList = new ArrayList<>();
        int maxRow = 0;
        for (int e : seats) {
            if (e > maxRow) {
                maxRow = e;
            }
        }
        SortUtils.DESC(studentGroups, "studentNo");
        int ssSum = 0;
        int max = 0;
        for (Group group : studentGroups) {
            ssSum = ssSum + group.getStudentNo();
            if (group.getStudentNo() > max) {
                max = group.getStudentNo();
            }
        }
        int middle = ssSum % 2 == 0 ? ssSum / 2 : ssSum / 2 + 1;

        List<String> listAll = new ArrayList<>();
        for (Group s : studentGroups) {
            for (int i = 0; i < s.getStudentNo(); i++) {
                listAll.add(s.getGroupId() + "_" + StringUtils.leftPad(i + "", 2, "0"));
            }
        }

        List<String> listOne = new ArrayList<>();
        List<String> listTwo = new ArrayList<>();

        listOne.addAll(listAll.subList(0, middle));
        if (max > middle) {
            int secondIndex = max - middle;
            listTwo.addAll(listAll.subList(middle + secondIndex, ssSum));
            listTwo.addAll(listAll.subList(middle, middle + secondIndex));
        } else {
            listTwo.addAll(listAll.subList(middle, ssSum));
        }

        Map<Integer, List<String>> map = new HashMap<>();
        map.put(0, listOne);
        map.put(1, listTwo);

        Map<String, String> seatMap = new HashMap<>();

        int maxCol = seats.size();

        for (int r = 0; r < maxRow; r++) {
            for (int c = 0; c < maxCol; c++) {
                int cr = (r % 2 + c) % 2;
                int listIndex = (r * maxCol + c - cr + (maxCol + 1) % 2 * (cr * 2 - c % 2 - cr)) / 2;
                List<String> list = map.get(cr);
                String seat = "";
                if (list.size() > listIndex)
                    seat = list.get(listIndex);
                else {
                    list = map.get(1 - cr);
                    listIndex = (r * maxCol + c - cr + (maxCol) % 2 * (cr * 2 - c % 2 - cr)) / 2;
                    if (list.size() > listIndex)
                        seat = list.get(listIndex);
                }
                while (listIndex < list.size() - 1 && StringUtils.isBlank(seat)) {
                    listIndex++;
                    seat = list.get(listIndex);
                }
                if (seats.get(c) < r + 1) {
                    if (list.size() > listIndex) {
                        if (StringUtils.isNotBlank(seat)) {
                            list.add(listIndex, "");
                        }
                    }
                    //没有该坐标的位子
                    System.out.print("  ■  \t | ");
                    Seat stuSeat = new Seat(c, r, listIndex, "-1", -2);
                    seatList.add(stuSeat);
                } else {
                    if (list.size() > listIndex) {
                        if (StringUtils.isBlank(seat)
                                || !checkValidate(seatMap, seat.split("_")[0], r, c, studentGroups.size(), maxRow)) {
                            System.out.print("  □  \t | ");
                            //排不了人
                            Seat stuSeat = new Seat(c, r, listIndex, "0", -1);
                            seatList.add(stuSeat);
                            if (StringUtils.isNotBlank(seat)) {
                                list.add(listIndex, "");
                            }
                        } else {
                            System.out.print(seat + "\t | ");
                            Seat stuSeat = new Seat(c, r, listIndex, seat.split("_")[0], 1);
                            seatList.add(stuSeat);
                            seatMap.put(r + "_" + c, seat.split("_")[0]);
                            list.remove(listIndex);
                            list.add(listIndex, "");
                        }
                    } else {
                        System.out.print("  □  \t | ");
                    }
                }
            }
        }
        return seatList;
    }

    public static boolean checkValidate(Map<String, String> seatMap, String seat, int r, int c, int maxColumn, int maxRow) {
        if (c - 1 >= 0 && (StringUtils.equals(seatMap.get(r + "_" + (c - 1)), seat)))
            return false;
        if (c + 1 <= maxColumn && StringUtils.equals(seatMap.get(r + "_" + (c + 1)), seat))
            return false;
        if (r - 1 >= 0 && StringUtils.equals(seatMap.get((r - 1) + "_" + c), seat))
            return false;
        if (r + 1 <= maxColumn && StringUtils.equals(seatMap.get((r + 1) + "_" + c), seat))
            return false;
        return true;
    }
}
