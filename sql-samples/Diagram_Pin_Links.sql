SELECT
        p1.uid || ' (id:' || p1.id || ', type: ' ||
        (CASE
           WHEN cmp1.componentType=0 THEN 'logic'
           WHEN cmp1.componentType=1 THEN 'input'
           WHEN cmp1.componentType=2 THEN 'output'
           ELSE 'unknown'
        END) || ')' "Pin 1",
        p2.uid || ' (id:' || p2.id || ', type: ' ||
        (CASE
           WHEN cmp2.componentType=0 THEN 'logic'
           WHEN cmp2.componentType=1 THEN 'input'
           WHEN cmp2.componentType=2 THEN 'output'
           ELSE 'unknown'
        END) || ')' "Pin 2"
FROM
        diagram_links dl
        INNER JOIN pin_links pl ON pl.id = dl.pinLinkId
        INNER JOIN pins p1 ON p1.id = pl.pin1
        INNER JOIN pins p2 ON p2.id = pl.pin2
        INNER JOIN connectors con1 ON con1.id = p1.connectorId
        INNER JOIN components cmp1 ON cmp1.id = con1.componentId
        INNER JOIN connectors con2 ON con2.id = p2.connectorId
        INNER JOIN components cmp2 ON cmp2.id = con2.componentId
WHERE
        dl.diagramId = 1;