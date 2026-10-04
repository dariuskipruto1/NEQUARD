CREATE TABLE alert_rule_states (
    id UUID PRIMARY KEY,
    rule_id UUID NOT NULL REFERENCES alert_rules(id) ON DELETE CASCADE,
    device_id UUID NOT NULL REFERENCES devices(id) ON DELETE CASCADE,
    consecutive_matches INTEGER NOT NULL DEFAULT 0,
    last_value DOUBLE PRECISION,
    last_evaluated_at TIMESTAMPTZ,
    UNIQUE(rule_id, device_id)
);
CREATE INDEX idx_alert_rule_states_device ON alert_rule_states(device_id);