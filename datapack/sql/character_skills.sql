-- ---------------------------
-- Table structure for character_skills
-- ---------------------------
CREATE TABLE IF NOT EXISTS character_skills (
  char_obj_id INT NOT NULL DEFAULT 0,
  skill_id INT NOT NULL DEFAULT 0,
  skill_level INT(3) NOT NULL DEFAULT 1,
  class_index INT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY  (char_obj_id,skill_id,class_index)
);
