import React, { Component } from "react";
import { List, ListItemText } from "@mui/material";
import Button from "@mui/material/Button";

const Toast = ({ message }) => {
  if (!message) return null;

  return (
    <div
      style={{
        position: "fixed",
        bottom: "20px",
        right: "20px",
        padding: "10px",
        backgroundColor: "black",
        color: "white",
        borderRadius: "5px",
        zIndex: 1000,
      }}
    >
      {message}
    </div>
  );
};

export default class Confirm extends Component {
  constructor(props) {
    super(props);
    this.state = {
      toastMessage: "",
    };
  }

  continue = async (e) => {
    e.preventDefault();
    // PROCESS FORM //
    this.props.nextStep();
    const values = this.props;
    console.log(JSON.stringify(values.values));
    const response = await fetch("http://127.0.0.1:8080/task/create", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(values.values),
    }).then((data) => {
      this.setState((prevState) => ({
        toastMessage: data.body,
      }));
      window.location.reload();
    });
  };

  back = (e) => {
    e.preventDefault();
    this.props.prevStep();
  };

  render() {
    const {
      values: { email, title, description },
    } = this.props;
    return (
      <List>
        <ListItemText primary="Please verify your contact information before clicking confirm..." />
        <hr />
        <ListItemText secondary="Email:  " />
        <ListItemText primary={email} />

        <ListItemText secondary="Title:  " />
        <ListItemText primary={title} />
        <ListItemText secondary="Description:  " />
        <ListItemText primary={description} />
        <hr />
        <Button label="Confirm" color="primary" onClick={this.continue}>
          Confirm
        </Button>
        <Toast message={this.state.toastMessage} />

        <Button label="Back" color="primary" onClick={this.back}>
          Back
        </Button>
      </List>
    );
  }
}
