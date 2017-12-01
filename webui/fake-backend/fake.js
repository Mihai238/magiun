// index.js

const faker = require('faker');

module.exports = () => {
  const data = {
    rows: [],
    datasets: [dataset1, dataset2]
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
    });
  }

  for (let i = 1000; i < 1100; i++) {
    data.rows.push({
      datasetId: "2",
      id: i,
      values: [
        faker.lorem.word(),
        faker.random.number({min: 1, max: 15}),
        faker.internet.color()
      ]
    });
  }

  return data;
};

const dataset1 = {
  id: 1,
  name: "people",
  schema: {
    columns: [
      {
        index: 0,
        name: "name",
        type: "string"
      },
      {
        index: 1,
        name: "age",
        type: "int"
      },
      {
        index: 2,
        name: "dateOfBirth",
        type: "date"
      },
      {
        index: 3,
        name: "happy",
        type: "boolean"
      }
    ]
  }
};

const dataset2 = {
  id: 2,
  name: "animals",
  schema: {
    columns: [
      {
        index: 0,
        name: "breed",
        type: "string"
      },
      {
        index: 1,
        name: "age",
        type: "int",
      },
      {
        index: 2,
        name: "color",
        type: "string"
      }
    ]
  }
};
