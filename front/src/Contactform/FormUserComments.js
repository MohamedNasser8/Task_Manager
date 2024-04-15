import React, { Component } from "react";
import TextField from "@mui/material/TextField";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DatePicker } from "@mui/x-date-pickers/DatePicker";
import { TimePicker } from "@mui/x-date-pickers/TimePicker";

import Button from "@mui/material/Button";
import { List, ListItem, ListItemText } from "@mui/material";

export default class FormUserComments extends Component {
  continue = (e) => {
    e.preventDefault();
    this.props.nextStep();
  };

  back = (e) => {
    e.preventDefault();
    this.props.prevStep();
  };

  render() {
    const { values, handleChange } = this.props;
    const canContinue =
      values.description.length > 0 &&
      values.startHour &&
      values.endHour &&
      values.day;
    const continueButton = (
      <Button color="primary" label="Continue" onClick={this.continue}>
        Continue
      </Button>
    );

    return (
      <List>
        <ListItemText primary="Please enter a description and continue to verify your information..." />
        <ListItem>
          <TextField
            multiline
            fullWidth
            id="outlined-multiline-flexible"
            label="Task description"
            rowsMax="4"
            margin="normal"
            variant="outlined"
            onChange={handleChange("description")}
            defaultValue={values.description}
          />
        </ListItem>

        <ListItem>
          <LocalizationProvider dateAdapter={AdapterDayjs}>
            <DatePicker
              label="Day"
              defaultValue={values.day}
              onChange={handleChange("day")}
            />
          </LocalizationProvider>
        </ListItem>

        <ListItem>
          <LocalizationProvider dateAdapter={AdapterDayjs}>
            <TimePicker
              value={values.startDate}
              label="Start Hour"
              onChange={handleChange("startHour")}
            />
          </LocalizationProvider>
        </ListItem>

        <ListItem>
          <LocalizationProvider dateAdapter={AdapterDayjs}>
            <TimePicker
              label="End Hour"
              value={values.endDate}
              onChange={handleChange("endHour")}
            />
          </LocalizationProvider>
        </ListItem>
        {canContinue ? continueButton : <Button disabled>Continue</Button>}
        <Button label="Back" color="primary" onClick={this.back}>
          Back
        </Button>
      </List>
    );
  }
}
