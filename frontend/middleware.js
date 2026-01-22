import {NextResponse} from 'next/server';

export function middleware(request) {
  const token = request.cookies.get('token');
  const protectedPaths = ['/pages/dashboard', '/pages/map'];
  const pathname = request.nextUrl.pathname;

  if (protectedPaths.includes(pathname) && !token) {
    return NextResponse.redirect(new URL('/auth/login', request.url));
  }

  return NextResponse.next();
}