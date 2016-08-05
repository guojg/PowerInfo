SELECT 
  s.id,
  s.wgwstdlyxss,
  s.sdshl,
  s.`pro_name` 
FROM
  senddata_itemname s 
  
  SELECT 'aaaa' AS 'sss','4444' AS 'bbbb' FROM DUAL;
 ---一下sql需要修改index_item 为senddata_itemname表中对应的某任务的类型的id
  
--1.控制月外购电力
SELECT 2 AS 'index_item',
       SUM(VALUE) VALUE,
       yr
  FROM
    senddata_data tb 
  WHERE index_item IN 
    (SELECT 
      id 
    FROM  senddata_itemname WHERE task_id=1 AND pid IS NOT NULL
    )   GROUP BY yr
--2.	500千伏交流   
SELECT 5 AS 'index_item',
	SUM(tb.value) VALUE,
	tb.yr
  FROM
    senddata_data tb 
  WHERE tb.index_item IN 
    (SELECT 
      id 
    FROM  senddata_itemname WHERE task_id=1 AND pid =5
    )   GROUP BY yr
--3.	特高压交流
SELECT 6 AS 'index_item',
	SUM(tb.value),
	tb.yr
  FROM
    senddata_data tb 
  WHERE index_item IN 
    (SELECT 
      id 
    FROM  senddata_itemname WHERE task_id=1 AND pid =6
    )   GROUP BY yr) b  ON a.id=b.index_item
--4.	直流 
SELECT 7 AS 'index_item',
SUM(tb.value),
tb.yr
  FROM
    senddata_data tb 
  WHERE index_item IN 
    (SELECT 
      id 
    FROM  senddata_itemname WHERE task_id=1 AND pro_name =7
    )   GROUP BY yr
   