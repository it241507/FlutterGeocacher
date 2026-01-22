'use client';

import * as React from "react";
import {useEffect, useState} from "react";
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import {Card, CircularProgress, Grid, TextField} from "@mui/material";
import FormButton from "@/app/components/button";
import {login} from "@/app/actions/userActions";
import {useRouter} from "next/navigation";

export default function LoginPage() {
  const router = useRouter();
  const [isMounted, setIsMounted] = useState(false);
  const [title, setTitle] = useState('Login')

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setIsMounted(true);
  }, []);

  const handleSubmit = async (e:any) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      await login(email, password);
      console.log('Login successful');
      router.push('/pages/map');
    } catch (err: any) {
      setError(err.message || 'Something went wrong');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container
      className="imgContainer"
      sx={{
        height: '100vh',
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
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
                  id="email-field"
                  variant="outlined"
                  type="email"
                  placeholder="Email*"
                  required
                  onChange={(e) => setEmail(e.target.value)}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  id="password-field"
                  variant="outlined"
                  type="password"
                  placeholder="Password*"
                  required
                  onChange={(e) => setPassword(e.target.value)}
                />
              </Grid>
              <Grid item xs={8}>
                {loading? <CircularProgress /> : <FormButton
                  label="LOGIN"
                  onClick={handleSubmit}
                  variant={"contained"}
                  type={"submit"}
                />}
              </Grid>
              {error && (
                <Grid item xs={12}>
                  <Typography color="error">{error}</Typography> {/* Display error message */}
                </Grid>
              )}
              <Grid item xs={12}>
                <p>Dont have an account? <a href="/auth/register">Register here</a></p>
              </Grid>
            </Grid>
          </form>
        </Card>
      </Box>
    </Container>
  );
}