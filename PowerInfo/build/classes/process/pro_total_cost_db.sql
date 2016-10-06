DELIMITER $$

USE `shiro`$$

DROP PROCEDURE IF EXISTS `pro_total_cost_db`$$

CREATE DEFINER=`root`@`%` PROCEDURE `pro_total_cost_db`(IN fdj_id_arg BIGINT,IN area_id_arg INT,IN task_id_arg BIGINT)
BEGIN 
--
DECLARE done INT DEFAULT 0;
DECLARE unit_fixed_record INT;
DECLARE unit_fixed_value INT;
DECLARE unit_change_value INT;
DECLARE unit_fixed_cur CURSOR FOR SELECT CODE FROM shiro.`sys_dict_table` WHERE domain_id=31;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
DELETE FROM generator_contrast_db WHERE jz_id=fdj_id_arg;
--  固定成本值
SELECT SUM(index_value) sumvalue INTO unit_fixed_value FROM shiro.constant_cost_arg_db WHERE index_type IN(9001,10001,11001,12001,13001) AND jz_id=fdj_id_arg AND task_id=task_id_arg;
-- 变动成本 （燃油，水，启动）
SELECT SUM(index_value) sumvalue INTO unit_change_value FROM shiro.constant_cost_arg_db WHERE index_type IN(15001,16001,17001) AND jz_id=fdj_id_arg AND task_id=task_id_arg;
-- 插入单位变动成本（燃煤，燃油，水，启动）
INSERT INTO  generator_contrast_db(index_x,unit,index_y,VALUE,jz_id,area_id,task_id)
SELECT b.code,6,6,a.value+unit_change_value,fdj_id_arg fdj_id,area_id_arg area_id ,task_id_arg task_id FROM (SELECT index_x,VALUE FROM coal_cost_data_fx  WHERE fdj_id=fdj_id_arg AND index_y=6 AND  task_id=task_id_arg) a  RIGHT JOIN (SELECT CODE FROM  sys_dict_table WHERE domain_id='31'
) b ON a.index_x=b.code;
-- 发电机功率
INSERT INTO  generator_contrast_db(index_x,unit,index_y,VALUE,jz_id,area_id,task_id)
SELECT b.code,1,1,a.value,fdj_id_arg fdj_id,area_id_arg area_id,task_id_arg task_id  FROM (SELECT index_x,VALUE FROM coal_cost_data_fx  WHERE fdj_id=fdj_id_arg AND index_y=1 AND task_id=task_id_arg) a  RIGHT JOIN (SELECT CODE FROM  sys_dict_table WHERE domain_id='31'
) b ON a.index_x=b.code;
OPEN unit_fixed_cur;
-- 开启游标
cursor_loop:LOOP
   FETCH unit_fixed_cur INTO unit_fixed_record; -- 取数据
   IF done=1 THEN
    LEAVE cursor_loop;
   END IF;
-- 插入单位固定成本
 INSERT INTO shiro.generator_contrast_db(index_x,index_y,unit,VALUE,jz_id,area_id,task_id) VALUES(unit_fixed_record,7,7,unit_fixed_value,fdj_id_arg,area_id_arg,task_id_arg);
 
END LOOP cursor_loop;
CLOSE unit_fixed_cur;
-- 插入单位总成本
INSERT INTO  generator_contrast_db(index_x,unit,index_y,VALUE,jz_id,area_id,task_id)
SELECT index_x,5,5,SUM(VALUE),fdj_id_arg fdj_id,area_id_arg area_id,task_id_arg task_id FROM shiro.`generator_contrast_db` WHERE index_y IN(6,7) AND jz_id=fdj_id_arg AND task_id=task_id_arg GROUP BY index_x;
-- 固定总成本
INSERT INTO  generator_contrast_db(index_x,unit,index_y,VALUE,jz_id,area_id,task_id)
SELECT t1.index_x,4,4,t1.value*t2.value*48*365 VALUE,fdj_id_arg jz_id,area_id_arg area_id,task_id_arg task_id FROM 
(SELECT VALUE,index_x FROM  generator_contrast_db WHERE index_y=1 AND jz_id=fdj_id_arg AND task_id=task_id_arg) t1  INNER JOIN 
(SELECT VALUE,index_x FROM  generator_contrast_db WHERE index_y=7 AND jz_id=fdj_id_arg AND task_id=task_id_arg) t2 ON t1.index_x=t2.index_x;
-- 变动总成本
INSERT INTO  generator_contrast_db(index_x,unit,index_y,VALUE,jz_id,area_id,task_id)
SELECT t1.index_x,3,3,t1.value*t2.value*48*365 VALUE,fdj_id_arg jz_id,area_id_arg area_id,task_id_arg task_id FROM 
(SELECT VALUE,index_x FROM  generator_contrast_db WHERE index_y=1 AND jz_id=fdj_id_arg AND task_id=task_id_arg) t1  INNER JOIN 
(SELECT VALUE,index_x FROM  generator_contrast_db WHERE index_y=6 AND jz_id=fdj_id_arg AND task_id=task_id_arg) t2 ON t1.index_x=t2.index_x;
-- 总成本
INSERT INTO  generator_contrast_db(index_x,unit,index_y,VALUE,jz_id,area_id,task_id)
SELECT index_x,2,2,SUM(VALUE),fdj_id_arg fdj_id,area_id_arg area_id,task_id_arg task_id FROM shiro.`generator_contrast_db` WHERE index_y IN(3,4) AND jz_id=fdj_id_arg AND task_id=task_id_arg GROUP BY index_x;
END$$

DELIMITER ;