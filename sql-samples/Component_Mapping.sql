SELECT
  c.uid || ' (id:' || c.id || ')'  "Component UID",
  c.className "Component Class",
  co.uid || ' (id:' || co.id || ', ' || CASEWHEN(co.bindingtype = 0, 'static', 'dynamic') || ')' "Connector UID",
  CASEWHEN(co.bindingtype = 0,
     co.connectorname,                         -- true
     co.connectorname || '[' || p.index || ']' -- false
  ) "Connector Name",
   p.uid "Pin UID",
   CASEWHEN(ph.valueType = 'java.lang.String',
     '"' || ph.value || '"', -- true
     ph.value                -- false
   ) || ' (' || ph.valueType || ')' "Pin Last Value"
FROM
        components c
        INNER JOIN connectors co ON c.id = co.componentid
        INNER JOIN pins p ON co.id = p.connectorid
        LEFT OUTER JOIN pin_values ph ON (p.id = ph.pinid AND ph.id = SELECT MAX(ph2.id) FROM pin_values ph2 WHERE p.id = ph2.pinid)
ORDER BY
        c.id, co.connectorname, p.index;