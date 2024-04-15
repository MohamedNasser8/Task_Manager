import React, { Component } from "react";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import { List, ListItem, ListItemText } from "@mui/material";

export default class FormContactInfo extends Component {
  continue = (e) => {
    e.preventDefault();
    this.props.nextStep();
  };

  render() {
    const { values, handleChange } = this.props;
    const canContinue = values.email.length > 0 && values.title.length > 0;
    const continueButton = (
      <Button color="primary" label="Continue" onClick={this.continue}>
        Continue
      </Button>
    );

    return (
      <List>
        <ListItemText
          primary="Please enter these information and continue to leave a
          description..."
        />
        <ListItem>
          <TextField
            required
            margin="dense"
            id="name"
            label="Email"
            type="email"
            fullWidth
            onChange={handleChange("email")}
            defaultValue={values.email}
          />
        </ListItem>

        <ListItem>
          <TextField
            required
            margin="dense"
            id="name"
            label="Title"
            type="text"
            fullWidth
            onChange={handleChange("title")}
            defaultValue={values.title}
          />
        </ListItem>
        {canContinue ? continueButton : <Button disabled>Continue</Button>}
      </List>
    );
  }
}
