'use client';

import * as React from "react";
import {useEffect, useState} from "react";
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import Link from '@mui/material/Link';
import NextLink from 'next/link';
import {Card, CircularProgress, Grid, TextField} from "@mui/material";
import FormButton from "@/app/components/button";
import {registration} from "@/app/actions/userActions";

export default function RegisterPage() {

  const [title, setTitle] = useState('Register')

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const [name, setName] = useState('');
  const [loading, setLoading] = useState<boolean>(false);


  const handleSubmit = async (e:any) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    if (password !== confirmPassword) {
      setError('Passwords do not match');
      setLoading(false);
      return;
    }

    try {
      await registration(email, name, password);
      console.log('Form successfully submitted:', { email, name, password });

      window.location.href = '/auth/login';
    } catch (err: any) {
      if (err.status === 422) {
        setError('Email already exists!');
      } else {
        setError(err.message || 'Something went wrong');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container maxWidth="lg">
      <Box
        sx={{
          my: 4,
          display: 'flex',
          flexDirection: 'column',
          justifyContent: 'center',
          alignItems: 'center',
        }}
      >
        <Typography variant="h1" component="h2" sx={{mb: 2}}>
          {title}
        </Typography>
        <Link href={"/auth/login"} color="secondary" component={NextLink}>
          Go to the login page
        </Link>
        <Card
          sx={{
            maxWidth: 345,
            mx: 'auto',
          }}
        >
          <form onSubmit={handleSubmit}>
            <Grid container spacing={2}
                  sx={{
                    justifyContent: 'center',
                    textAlign: 'center',
                    alignContent: 'center',
                    alignItems: 'center',
                  }}
            >
              <Grid item xs={12}>
                <h3>Login</h3>
              </Grid>
              <Grid item xs={12}>
                <TextField
                  id="outlined-basic"
                  variant="outlined"
                  type="email"
                  placeholder="Email*"
                  required
                  onChange={(e) => setEmail(e.target.value)}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  id="outlined-basic"
                  variant="outlined"
                  type="text"
                  placeholder="Name*"
                  required
                  onChange={(e) => setName(e.target.value)}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  id="outlined-basic"
                  variant="outlined"
                  type="password"
                  placeholder="Password*"
                  required
                  onChange={(e) => setPassword(e.target.value)}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  id="outlined-basic"
                  variant="outlined"
                  type="password"
                  placeholder="Confirm Password*"
                  required
                  onChange={(e) => setConfirmPassword(e.target.value)}
                />
              </Grid>

              {error && (
                <Grid item xs={12}>
                  <Typography color="error">{error}</Typography>
                </Grid>
              )}

              <Grid item xs={8}>
                {loading? <CircularProgress /> : <FormButton
                  label="REGISTER"
                  onClick={handleSubmit}
                  variant={"contained"}
                  type={"submit"}
                />}
              </Grid>
              <Grid item xs={12}>
                <p>Already have an account? <a href="./login">Login here</a></p>
              </Grid>
            </Grid>
          </form>
        </Card>
      </Box>
    </Container>
  );
}