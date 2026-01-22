import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import Container from '@mui/material/Container';
import PowerSettingsNewIcon from '@mui/icons-material/PowerSettingsNew';
import { handleLogout } from '@/app/actions/userActions';

export default function NavBar() {
  return (
    <AppBar position="fixed" id="navbar" style={{ zIndex: 1300, top: 0 }}>
      <Container maxWidth="lg">
        <Toolbar>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            Geocache
          </Typography>
          <Button color="inherit" href="/pages/map">Map</Button>
          <Button color="inherit" href="/pages/dashboard">Profile</Button>
          <Button color="inherit" onClick={() => handleLogout(document.cookie)} href="/auth/login">
            <PowerSettingsNewIcon />
          </Button>
        </Toolbar>
      </Container>
    </AppBar>
  );
}