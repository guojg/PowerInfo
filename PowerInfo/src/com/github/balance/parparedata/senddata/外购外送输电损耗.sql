--3.	输电损耗
INSERT INTO senddata_data (yr,index_item,VALUE)
SELECT 
   yr,
   SUM(a.`sdshl`*b.value),
   '3' index_item
FROM
  senddata_itemname  a
  RIGHT JOIN 
    (SELECT 
      VALUE,
      yr,
      index_item 
    FROM
      senddata_data 
    WHERE index_item IN 
    (SELECT 
      id 
    FROM  senddata_itemname WHERE task_id=1 AND pid IS NOT NULL
     )
     ) b ON a.`id`=b.index_item GROUP  BY b.yr
UNION ALL     
--4.	控制月外购电力（含损耗）
SELECT 
   yr,
   SUM(b.value)-SUM(a.`sdshl`*b.value),
   '1' index_item

FROM
  senddata_itemname  a
  RIGHT JOIN 
    (SELECT 
      VALUE,
      yr,
      index_item 
    FROM
      senddata_data 
    WHERE index_item IN 
    (SELECT 
      id 
    FROM  senddata_itemname WHERE task_id=1 AND pid IS NOT NULL
     )
     ) b ON a.`id`=b.index_item GROUP  BY b.yr
 UNION ALL
--4.外购电量
SELECT 
   b.yr,
   SUM(b.value*a.wgwstdlyxss/10000),
   '4' index_item
FROM
  senddata_itemname  a
  RIGHT JOIN 
    (SELECT 
      VALUE,
      yr,
      index_item 
    FROM
      senddata_data 
    WHERE index_item IN 
    (SELECT 
      id 
    FROM  senddata_itemname WHERE task_id=1 AND pid IS NOT NULL
     )
     ) b ON a.`id`=b.index_item GROUP  BY b.yr