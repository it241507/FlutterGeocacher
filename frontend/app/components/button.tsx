import * as React from 'react';
import Button from "@mui/material/Button";

interface FormButtonProps {
  label: string;
  onClick: (e: React.MouseEvent<HTMLButtonElement>) => void;
  variant?: "text" | "outlined" | "contained";
  type: "button" | "submit" | "reset";
  fullWidth?: boolean;
  disabled?: boolean;
  href?: string;
}

export function FormButton(props: FormButtonProps) {
  return (
    <Button
      variant={props.variant || "contained"}
      onClick={props.onClick}
      type={props.type}
      fullWidth={props.fullWidth}
      disabled={props.disabled}
      href={props.href}
    >
      {props.label}
    </Button>
  );
}

export default FormButton;
