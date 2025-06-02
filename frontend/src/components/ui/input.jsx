import React from "react";
import { cn } from "@/lib/utils";

const Input = ({ className, type, variant = "default", ...props }) => {
  return (
    <input
      type={type}
      className={cn(
        "flex h-9 w-full rounded-md border border-input bg-background px-3 py-1 text-sm shadow-sm focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2 file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground disabled:cursor-not-allowed disabled:opacity-50",
        variant === "outline" && "border-input",
        className
      )}
      {...props}
    />
  );
};

Input.displayName = "Input";
export default Input;
