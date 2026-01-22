'use client'

import * as React from "react";
import Container from '@mui/material/Container';
import Box from "@mui/material/Box";
import {Card, CardContent} from "@mui/material";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import Link from "@mui/material/Link";
import NextLink from "next/link";

export default function HomePage() {

  return (
    <Container
      className="imgContainer"
      sx={{
        height: '100vh',
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
        maxWidth: '100vw',
      }}
    >
      <Box
        sx={{
          my: 4,
          display: 'flex',
          flexDirection: 'column',
          justifyContent: 'center',
          alignItems: 'center',
        }}
      >
        <Card sx={{
          minWidth: 275,
          backdropFilter: 'blur(10px)',
          backgroundColor: 'rgba(255, 255, 255, 0.5)',
          borderRadius: 2,
          boxShadow: '0 8px 32px rgba(0, 0, 0, 0.4)',
          border: '1px solid rgba(255, 255, 255, 0.3)',
        }}
        >
          <CardContent sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
            <Typography variant="h1" component="h2" sx={{mb: 2}}>
              GeoCache
            </Typography>
            <Button variant="contained" href="/auth/login">
              Login
            </Button>
            <Link href="/auth/register" color="secondary" component={NextLink} sx={{ mt: 2 }}>
              Don&#39;t have an account? Sign Up
            </Link>
          </CardContent>
        </Card>
      </Box>
    </Container>
  );
}