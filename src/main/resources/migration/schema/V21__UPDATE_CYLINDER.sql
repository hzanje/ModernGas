ALTER TABLE `cylinder` CHANGE last_service hydroTestingDate datetime NULL;
ALTER TABLE `cylinder` CHANGE next_service next_hydro_test_due_date datetime NULL;