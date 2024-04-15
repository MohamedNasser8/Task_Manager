import React, { Component } from "react";
import FormContactInfo from "./FormConatctInfo";
import FormUserComments from "./FormUserComments";
import Confirm from "./Confirm";
import Success from "./Success";

export default class UserForm extends Component {
  state = {
    step: 1,
    email: "",
    title: "",
    description: "",
    day: null,
    startHour: null,
    endHour: null,
  };

  // Proceed to next step
  nextStep = () => {
    const { step } = this.state;
    this.setState({
      step: step + 1,
    });
  };

  // Go back to prev step
  prevStep = () => {
    const { step } = this.state;
    this.setState({
      step: step - 1,
    });
  };

  // Handle fields change
  handleChange = (input) => (e) => {
    if (input === "day") {
      this.setState({ [input]: e.format("YYYY-MM-DD") });
    } else if (input === "startHour" || input === "endHour") {
      this.setState({ [input]: e.format("HH:mm:ss") });
    } else this.setState({ [input]: e.target.value });
  };

  render() {
    const { step } = this.state;
    const { email, title, description, day, startHour, endHour } = this.state;
    const values = { email, title, description, day, startHour, endHour };

    switch (step) {
      case 1:
        return (
          <FormContactInfo
            nextStep={this.nextStep}
            handleChange={this.handleChange}
            values={values}
          />
        );
      case 2:
        return (
          <FormUserComments
            nextStep={this.nextStep}
            prevStep={this.prevStep}
            handleChange={this.handleChange}
            values={values}
          />
        );
      case 3:
        return (
          <Confirm
            nextStep={this.nextStep}
            prevStep={this.prevStep}
            values={values}
          />
        );
      case 4:
        return <Success values={values} />;
    }
  }
}
