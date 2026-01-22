enum Mode {
  SERVER,
  LOCAL
}

// change this to use sever or local backend
export const mode = Mode.LOCAL

// @ts-ignore
// automatically changes depending on mode
export const baseUrl = mode === Mode.LOCAL ? 'http://localhost:8083' : 'https://lbartner-01.media.fhstp.ac.at:4433'