DELIMITER $$

USE `shiro`$$

DROP PROCEDURE IF EXISTS `pro_total_cost_fdc`$$

CREATE DEFINER=`root`@`%` PROCEDURE `pro_total_cost_fdc`(IN fdc_id_arg INT,IN area_id_arg INT)
BEGIN 
--

DELETE FROM shiro.`electricity_contrast` WHERE dc_id=fdc_id_arg;


INSERT INTO  electricity_contrast(index_x,unit,index_y,VALUE,dc_id,area_id)
SELECT  index_x,unit,index_y,SUM(VALUE),fdc_id_arg dc_id,area_id_arg area_id FROM generator_contrast WHERE jz_id IN
(SELECT  jz_id FROM constant_cost_arg WHERE  index_type=200 AND index_value=fdc_id_arg) AND index_y IN(2,3,4,5,6,7) GROUP BY index_x,index_y,unit;
END$$

DELIMITER ;