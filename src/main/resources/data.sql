INSERT INTO customer_status (customer_status_id, customer_status_name)VALUES(1, 'internal');
INSERT INTO customer_status(customer_status_id, customer_status_name)VALUES(2, 'active');
INSERT INTO customer_status(customer_status_id, customer_status_name)VALUES(3, 'inactive');
INSERT INTO customer_status(customer_status_id, customer_status_name)VALUES(4, 'pending');
INSERT INTO "Organizations"
(id, "name", created_at, updated_at, replay, replay_flags, entity_overwrites, proc_disabled, disabled_reason, disable_if_err, alias_organization_id, use_case_sensitive_ids, customer, proc_metadata, retain, customer_status, um_owner, customer_contact, "version", configurations)
VALUES(28, 'Przemek Test 01', '2015-01-26 23:35:48.226', '2015-01-26 23:35:48.226', false, 12, NULL, false, 'suspend while rebalancing storm', false, NULL, false, false, NULL, false, 1, NULL, NULL, 1, NULL);
