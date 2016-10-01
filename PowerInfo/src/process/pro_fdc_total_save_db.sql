DELIMITER $$

USE `shiro`$$

DROP PROCEDURE IF EXISTS `pro_fdc_total_save_db`$$

CREATE DEFINER=`root`@`%` PROCEDURE `pro_fdc_total_save_db`(IN fdc_id_arg INT,IN area_id_arg INT,IN task_id_arg BIGINT)
BEGIN  
DECLARE done INT DEFAULT 0; 
DECLARE fdj_list_record INT; -- 游标子值
DECLARE fdj_list_cur CURSOR FOR SELECT jz_id FROM shiro.`constant_cost_arg_fx` WHERE  index_type=200 AND index_value=fdc_id_arg;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;      
CALL shiro.`half_fdj_assigned_db`(fdc_id_arg,area_id_arg,task_id_arg);   
OPEN fdj_list_cur;
-- 开启游标
cursor_loop:LOOP
   FETCH fdj_list_cur INTO fdj_list_record; -- 取数据
   IF done=1 THEN
    LEAVE cursor_loop;
   END IF;
   CALL shiro.`pro_total_cost_db`(fdj_list_record,area_id_arg,task_id_arg);   
END LOOP cursor_loop;
CLOSE fdj_list_cur;
CALL shiro.`pro_total_cost_fdc_db`(fdc_id_arg,area_id_arg,task_id_arg);   
END$$

DELIMITER ;