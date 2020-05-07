package net.zdsoft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class Coloring {

	private class Seat {
		private int posX;
		private int posY;
		private String groupId;
		private int index;
		private int state; //1 = 正常；-1=空；-2=不可用

		public Seat(int posX, int posY, int index, String groupId, int state) {
			this.posX = posX;
			this.posY = posY;
			this.index = index;
			this.state = state;
			this.groupId = groupId;
		}

		public int getPosX() {
			return posX;
		}

		public void setPosX(int posX) {
			this.posX = posX;
		}

		public int getPosY() {
			return posY;
		}

		public void setPosY(int posY) {
			this.posY = posY;
		}

		public String getGroupId() {
			return groupId;
		}

		public void setGroupId(String groupId) {
			this.groupId = groupId;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public int getState() {
			return state;
		}

		public void setState(int state) {
			this.state = state;
		}
	}

	private class Group {
		private String groupId;
		private int studentNo;

		public String getGroupId() {
			return groupId;
		}

		public void setGroupId(String groupId) {
			this.groupId = groupId;
		}

		public int getStudentNo() {
			return studentNo;
		}

		public void setStudentNo(int studentNo) {
			this.studentNo = studentNo;
		}
	}

	public static void main(String[] args) {
		List<Integer> seats = Arrays.asList(5, 8, 8, 7, 8,7,8); // 座位信息
//		List<Integer> studentGroups = Arrays.asList(10, 5, 8, 7,3,3,2); // 班级人数信息
		List<Integer> studentGroups = Arrays.asList(25,  7,3,3); // 班级人数信息
		Coloring color = new Coloring();
		color.createSeats(seats, studentGroups);
	}

	private List<Seat> createSeats(List<Integer> seats, List<Integer> studentGroups) {
		List<Seat> seatList = new ArrayList<>();
		int maxRow = seats.stream().mapToInt(k -> k).max().getAsInt();
		studentGroups.sort((o1, o2) -> o2 - o1);
		int ssSum = studentGroups.stream().mapToInt(k -> k).sum();
		int middle = ssSum % 2 == 0 ? ssSum / 2 : ssSum / 2 + 1;
		int max = studentGroups.stream().mapToInt(k -> k).max().getAsInt();

		List<String> listAll = new ArrayList<>();
		int index = 0;
		for (int s : studentGroups) {
			for (int i = 0; i < s; i++) {
				listAll.add((char) ('a' + index) + StringUtils.leftPad(i + "", 2, "0"));
			}
			index++;
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
					System.out.print("  ■  \t | ");
					Seat stuSeat = new Seat(c, r, listIndex, "-1", -2);
					seatList.add(stuSeat);
				} else {
					if (list.size() > listIndex) {
						if (StringUtils.isBlank(seat)
								|| !checkValidate(seatMap, seat.charAt(0) + "", r, c, studentGroups.size(), maxRow)) {
							System.out.print("  □  \t | ");
							Seat stuSeat = new Seat(c, r, listIndex, "0", -1);
							seatList.add(stuSeat);
							if (StringUtils.isNotBlank(seat)) {
								list.add(listIndex, "");
							}
						} else {
							System.out.print(seat + "\t | ");
							Seat stuSeat = new Seat(c, r, listIndex, seat.charAt(0) + "", 1);
							seatList.add(stuSeat);
							seatMap.put(r + "_" + c, seat.charAt(0) + "");
							list.remove(listIndex);
							list.add(listIndex, "");
						}
					} else {
						System.out.print("  □  \t | ");
					}
				}
			}
			System.out.println();
		}

//		System.out.print("c'\t");
//		System.out.print("r\t");
//		System.out.print("c\t");
//		System.out.print("i\t");
//		System.out.print("g\t");
//		System.out.println();
//		int mr = 15;
//		int mc = 9;
//		for (int row = 0; row < mr; row++) {
//			for (int col = 0; col < mc; col++) {
//				System.out.print(row * mc + col + "\t");
//				System.out.print(row + "\t");
//				System.out.print(col + "\t");
//				System.out.print((row * mc + col) / 2 + "\t");
//				System.out.print((row % 2 + col) % 2 + "\t");
//				int cr = (row % 2 + col) % 2;
//				System.out.print((row * mc + col - cr + (mc + 1) % 2 * (cr * 2 - col % 2 - cr)) / 2 + "\t");
//
//				System.out.println();
//			}
//		}
		
		return seatList;
	}

	private boolean checkValidate(Map<String, String> seatMap, String seat, int r, int c, int maxColumn, int maxRow) {
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
