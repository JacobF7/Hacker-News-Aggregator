CREATE TABLE user (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255) NOT NULL UNIQUE ,
  `password` VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE session (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) NOT NULL,
  `token` VARCHAR(255) NOT NULL UNIQUE,
  `last_activity` BIGINT(22) NOT NULL,

  PRIMARY KEY (id),
  FOREIGN KEY user_fk (user_id) REFERENCES user(id)
) ENGINE = InnoDB;

CREATE TABLE story (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `hacker_news_id` BIGINT(22) NOT NULL UNIQUE,
  `title` VARCHAR(255) NOT NULL,
  `author` VARCHAR(255) NOT NULL,
  `url` VARCHAR(511) NOT NULL,
  `score` INT(11) NOT NULL,
  `deleted` BOOLEAN NOT NULL,
  `last_updated` BIGINT(22) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

CREATE TABLE topic (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL UNIQUE ,
  `top_story_id` INT(11) ,
  PRIMARY KEY (id),
  FOREIGN KEY story_fk (top_story_id) REFERENCES story(id) ON UPDATE CASCADE
) ENGINE = InnoDB;

CREATE TABLE user_topic (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) NOT NULL,
  `topic_id` INT(10)  NOT NULL,
  `effective_from` BIGINT(22) NULL,
  `effective_to` BIGINT(22) NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY user_fk (user_id) REFERENCES user(id) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY topic_fk (topic_id) REFERENCES topic(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;
