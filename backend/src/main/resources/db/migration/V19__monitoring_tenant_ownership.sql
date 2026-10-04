ALTER TABLE alerts ADD COLUMN IF NOT EXISTS organization_id UUID REFERENCES organizations(id) ON DELETE CASCADE;
ALTER TABLE incidents ADD COLUMN IF NOT EXISTS organization_id UUID REFERENCES organizations(id) ON DELETE CASCADE;
CREATE INDEX IF NOT EXISTS idx_alerts_org_status ON alerts(organization_id,status);
CREATE INDEX IF NOT EXISTS idx_incidents_org_status ON incidents(organization_id,status);