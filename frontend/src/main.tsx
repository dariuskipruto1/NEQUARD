import React from 'react';
import {createRoot} from 'react-dom/client';
import './styles.css';
function App(){return <main><header><strong>NEQUARD</strong><span>INTELLIGENT NETWORK OPERATIONS</span><b>PHASE 1</b></header><section><small>NETWORK • CONNECTIVITY • SECURITY • COMMUNITY</small><h1>One intelligent platform for the networks that matter.</h1><p>Monitoring, incidents, diagnostics, connectivity and community infrastructure in one operational system.</p><div className="grid"><article>BACKEND<strong>Spring Boot 3 · Java 21</strong></article><article>DATA<strong>PostgreSQL · Flyway</strong></article><article>INTELLIGENCE<strong>Python ML service</strong></article><article>DEPLOYMENT<strong>Docker · Render</strong></article></div></section><footer>Developed by Darius Kipruto</footer></main>}
createRoot(document.getElementById('root')!).render(<React.StrictMode><App/></React.StrictMode>);
