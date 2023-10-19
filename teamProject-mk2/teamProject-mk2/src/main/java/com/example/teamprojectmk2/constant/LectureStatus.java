package com.example.teamprojectmk2.constant;

/*public enum LectureStatus {
   ONE_TO_ONE, GROUP, ONLINE
}*/
public enum LectureStatus {
   ONE_TO_ONE("1:1 강의"), GROUP("그룹 강의"), ONLINE("온라인 강의");

   private final String description;

   LectureStatus(String description) {
      this.description = description;
   }

   public String getDescription() {
      return description;
   }
}
