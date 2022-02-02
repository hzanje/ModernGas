ALTER TABLE cylinder ADD CONSTRAINT FK_CYLINDER_DETAILS_ID FOREIGN KEY (cylinder_detail_id) REFERENCES cylinder_inventory_details(id);
