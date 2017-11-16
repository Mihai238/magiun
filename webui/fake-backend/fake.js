// index.js

const faker = require('faker');

module.exports = () => {
  const data = {
    rows: [],
    datasets: [dataset1, dataset2, dataset3]
  };

  for (let i = 0; i < 1000; i++) {
    data.rows.push({
      datasetId: "1",
      id: i,
      values: [
        faker.name.findName(),
        faker.random.number({min: 18, max: 99}),
        faker.date.past(),
        faker.random.boolean()
      ]
    })
  }

  return data;
};

const dataset1 = {
  id: 1,
  name: "people",
  schema: {
    columns: [
      {
        name: "string"
      },
      {
        age: "int"
      },
      {
        dateOfBirth: "date"
      },
      {
        happy: "boolean"
      }
    ]
  }
};

const dataset2 = {
  "id": 2,
  "name": "animals",
  "schema": {
    "columns": [
      {
        "breed": "string"
      },
      {
        "age": "int"
      },
      {
        "color": "string"
      }
    ]
  }
};

const dataset3 = {
  "id": 3,
  "name": "another_test dataset",
  "schema": {
    "columns": [
      {
        "column1": "string"
      },
      {
        "column2": "int"
      },
      {
        "column3": "boolean"
      }
    ]
  }
};

