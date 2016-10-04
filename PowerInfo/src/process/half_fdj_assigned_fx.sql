DELIMITER $$

USE `shiro`$$

DROP PROCEDURE IF EXISTS `half_fdj_assigned_fx`$$

CREATE DEFINER=`root`@`%` PROCEDURE `half_fdj_assigned_fx`(IN fdc_id_arg INT,IN area_id_arg INT,IN task_id_arg BIGINT)
BEGIN 
DECLARE done INT DEFAULT 0;
DECLARE fdj_count INT; -- 机组个数
DECLARE materials_cost_arg INT; -- 材料费
DECLARE salary_arg INT; -- 工资、福利
DECLARE repairs_cost_arg INT; -- 修理
DECLARE fdj_list_record INT; -- 游标子值
DECLARE other_cost_arg INT; -- 其他
DECLARE fdj_list_cur CURSOR FOR SELECT jz_id FROM shiro.`constant_cost_arg_fx` WHERE  index_type=200 AND index_value=fdc_id_arg AND task_id=task_id_arg;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
DELETE FROM shiro.constant_cost_arg_fx  WHERE index_type IN(10001,11001,12001,13001) AND jz_id IN(SELECT a.jz_id FROM (SELECT  jz_id FROM constant_cost_arg_fx WHERE  index_type=200 AND index_value=fdc_id_arg AND task_id=task_id_arg) a );
-- 获取机组在电厂下有几个机组
SELECT COUNT(1)  INTO fdj_count  FROM shiro.constant_cost_arg_fx WHERE index_type=200 AND index_value=fdc_id_arg AND task_id=task_id_arg;
-- 材料费
SELECT materials_cost INTO materials_cost_arg FROM shiro.`electricpowerplant_analysis_data_fx` WHERE id=fdc_id_arg AND task_id=task_id_arg;
-- 工资、福利
SELECT salary INTO salary_arg  FROM shiro.`electricpowerplant_analysis_data_fx` WHERE id=fdc_id_arg AND task_id=task_id_arg;
-- 修理
SELECT repairs_cost INTO repairs_cost_arg FROM shiro.`electricpowerplant_analysis_data_fx` WHERE id=fdc_id_arg AND task_id=task_id_arg;
-- 其他
SELECT other_cost INTO other_cost_arg FROM shiro.`electricpowerplant_analysis_data_fx` WHERE id=fdc_id_arg AND task_id=task_id_arg;
-- 删除对应的数据
-- 开启游标
OPEN fdj_list_cur;
-- 开启游标
cursor_loop:LOOP
   FETCH fdj_list_cur INTO fdj_list_record; -- 取数据
   IF done=1 THEN
    LEAVE cursor_loop;
   END IF;
-- 插入材料
 INSERT INTO shiro.constant_cost_arg_fx(index_type,index_value,jz_id,area_id,task_id) VALUES(10001,materials_cost_arg/fdj_count,fdj_list_record,area_id_arg,task_id_arg);
-- 工资、福利
 INSERT INTO shiro.constant_cost_arg_fx(index_type,index_value,jz_id,area_id,task_id) VALUES(11001,salary_arg/fdj_count,fdj_list_record,area_id_arg,task_id_arg);
-- 修理
 INSERT INTO shiro.constant_cost_arg_fx(index_type,index_value,jz_id,area_id,task_id) VALUES(12001,repairs_cost_arg/fdj_count,fdj_list_record,area_id_arg,task_id_arg);
-- 其他
 INSERT INTO shiro.constant_cost_arg_fx(index_type,index_value,jz_id,area_id,task_id) VALUES(13001,other_cost_arg/fdj_count,fdj_list_record,area_id_arg,task_id_arg);
END LOOP cursor_loop;
CLOSE fdj_list_cur;
END$$

DELIMITER ;