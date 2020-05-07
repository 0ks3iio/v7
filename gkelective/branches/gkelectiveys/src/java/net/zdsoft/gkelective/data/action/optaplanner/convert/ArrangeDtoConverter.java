package net.zdsoft.gkelective.data.action.optaplanner.convert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zdsoft.gkelective.data.action.optaplanner.domain.ArrangeClassRoom;
import net.zdsoft.gkelective.data.action.optaplanner.domain.ArrangeStudent;
import net.zdsoft.gkelective.data.dto.Room;
import net.zdsoft.gkelective.data.dto.StudentSubjectDto;

public class ArrangeDtoConverter {

    public static List<ArrangeStudent> convertToArrangeStudent(List<StudentSubjectDto> students) {
        List<ArrangeStudent> arrangeStudentList = new ArrayList<ArrangeStudent>();
        int i = 1;
        ArrangeStudent arrangeStudent = null;
        for (StudentSubjectDto studentSubjectDto : students) {
            arrangeStudent = new ArrangeStudent(); 
            arrangeStudent.setId(i);
            arrangeStudent.setStudentId(studentSubjectDto.getStuId());
            arrangeStudent.setStudentSubjectDto(studentSubjectDto);
            arrangeStudent.setAllSubjectList(studentSubjectDto.getAllSubjectIds());
            arrangeStudent.setChooseSubjectIds(studentSubjectDto.getChooseSubjectIds());
            arrangeStudentList.add(arrangeStudent);
            i++;
        }
        return arrangeStudentList;
    }
    
    public static Map<String, List<Room>>[] convertToRoomResultMapArray(Map<String, List<ArrangeClassRoom>>[] cellBottleArray) {
        Map<String, List<Room>>[] resultMaps = new HashMap[cellBottleArray.length];
        Map<String, List<Room>> resultMap = null;
        Map<String, List<ArrangeClassRoom>> map = null;
        for (int i = 0; i < cellBottleArray.length; i++) {
            resultMap= new HashMap<String, List<Room>>();
            map = cellBottleArray[i];
            for (Map.Entry<String, List<ArrangeClassRoom>> entry : map.entrySet()) {
                resultMap.put(entry.getKey(), convertToRoomList(entry.getValue()));
            }
            resultMaps[i] = resultMap;
        }
        return resultMaps;
    }
    
    public static List<Room> convertToRoomList(List<ArrangeClassRoom> classRooms) {
        List<Room> roomList = new ArrayList<Room>();
        List<ArrangeStudent> studentList = null;
        String subjectId = null;
        String roomType = null;
        int batch = 0;
        Room room = null;
        List<StudentSubjectDto> studentSubjectDtoList= null;
        StudentSubjectDto studentSubjectDto = null;
        for (ArrangeClassRoom classRoom : classRooms) {
            batch = classRoom.getBatch();
            roomType = classRoom.getRoomType();
            studentList = classRoom.getStudentList();
            subjectId = classRoom.getSubjectId();
            
            room = new Room();
            room.setLevel(classRoom.getLevel());
            room.setBatch(batch);
            room.setSubjectId(subjectId);
            room.setType(roomType);
            studentSubjectDtoList= new ArrayList<StudentSubjectDto>();
            for (ArrangeStudent arrangeStudent : studentList) {
                studentSubjectDto = arrangeStudent.getStudentSubjectDto();
                studentSubjectDtoList.add(studentSubjectDto);
            }
            room.setStudentList(studentSubjectDtoList);
            roomList.add(room);
        }
        return roomList;
    }
}
