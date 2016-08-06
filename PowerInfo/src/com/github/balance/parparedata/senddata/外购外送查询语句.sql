 SELECT b.value 'pro_name',b.`sdshl`,b.`wgwstdlyxss`,b.`ID`,SUM(CASE a.yr WHEN '2015' THEN a.`value` END) '2015' FROM senddata_data  a
 RIGHT JOIN (SELECT a.id ,a.pro_name,b.value,a.task_id,a.wgwstdlyxss,a.sdshl FROM `senddata_itemname` a  RIGHT JOIN sys_dict_table b ON pro_name=b.code WHERE b.domain_id=18 AND b.code IN(1,2,3,4,5)) b ON b.id=a.`index_item` WHERE b.`task_id`=1 GROUP BY pro_name,sdshl,wgwstdlyxss 
 
 UNION ALL
 SELECT b.pro_name,b.`sdshl`,b.`wgwstdlyxss`,b.`ID`,SUM(CASE a.yr WHEN '2015' THEN a.`value` END) '2015' FROM senddata_data a RIGHT JOIN senddata_itemname  b  ON a.`index_item`=b.id WHERE b.pid='5'
 UNION ALL
 SELECT b.value 'pro_name',b.`sdshl`,b.`wgwstdlyxss`,b.`ID`,SUM(CASE a.yr WHEN '2015' THEN a.`value` END) '2015' FROM senddata_data  a
 RIGHT JOIN ( SELECT a.id ,b.value,a.pro_name,a.task_id,a.wgwstdlyxss,a.sdshl FROM `senddata_itemname` a  RIGHT JOIN sys_dict_table b ON pro_name=b.code WHERE b.domain_id=18 AND b.code=6) b ON b.id=a.`index_item` WHERE b.`task_id`=1 GROUP BY pro_name,sdshl,wgwstdlyxss 
 UNION ALL
 SELECT b.pro_name,b.`sdshl`,b.`wgwstdlyxss`,b.`ID`,SUM(CASE a.yr WHEN '2015' THEN a.`value` END) '2015' FROM senddata_data a RIGHT JOIN senddata_itemname  b  ON a.`index_item`=b.id WHERE b.pid='6'
 UNION ALL
 SELECT b.value 'pro_name',b.`sdshl`,b.`wgwstdlyxss`,b.`ID`,SUM(CASE a.yr WHEN '2015' THEN a.`value` END) '2015' FROM senddata_data  a
 RIGHT JOIN ( SELECT a.id ,b.value,a.pro_name,a.task_id,a.wgwstdlyxss,a.sdshl FROM `senddata_itemname` a  RIGHT JOIN sys_dict_table b ON pro_name=b.code WHERE b.domain_id=18 AND b.code=7) b ON b.id=a.`index_item` WHERE b.`task_id`=1 GROUP BY pro_name,sdshl,wgwstdlyxss 
 UNION ALL
 SELECT b.pro_name,b.`sdshl`,b.`wgwstdlyxss`,b.`ID`,SUM(CASE a.yr WHEN '2015' THEN a.`value` END) '2015' FROM senddata_data a RIGHT JOIN senddata_itemname  b  ON a.`index_item`=b.id WHERE b.pid='7'


 
 
 
