SELECT
  c.uid || ' (id:' || c.id || ')' "Component UID",
  c.className "Class Name",
  '(x:' || dc.positionX || ', y:' || dc.positionY || ')' "Component Position"
FROM
  diagram_components dc
  INNER JOIN components c ON dc.componentId = c.id
  WHERE dc.diagramId = 1
;