// index.js

const faker = require('faker');

module.exports = () => {
  const data = {
    rows: [],
    datasets: [dataset1, dataset2],
    blocks: blocks
  };

  for (let i = 0; i < 1000; i++) {
    let height = faker.random.number({min: 1.40, max: 2.20, precision: 0.01});

    let sportLevel = ['low', 'medium', 'high'];

    data.rows.push({
      datasetId: "1",
      id: i,
      values: [
        faker.name.findName(),
        faker.random.number({min: 18, max: 99}).toString(),
        faker.date.past(),
        faker.random.boolean().toString(),
        height.toString(),
        (((height * 100) - 100) + faker.random.number({min: -20, max: 20})).toString(),
        sportLevel[faker.random.number({min: 0, max: 2})]
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
        type: "String"
      },
      {
        index: 1,
        name: "age",
        type: "Int"
      },
      {
        index: 2,
        name: "dateOfBirth",
        type: "Date"
      },
      {
        index: 3,
        name: "happy",
        type: "Boolean"
      },
      {
        index: 4,
        name: "height",
        type: "Double"
      },
      {
        index: 5,
        name: "weight",
        type: "Double"
      },
      {
        index: 6,
        name: "sport level",
        type: "String"
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
        type: "String"
      },
      {
        index: 1,
        name: "age",
        type: "Int",
      },
      {
        index: 2,
        name: "color",
        type: "String"
      }
    ]
  }
};

const blocks = [
  {
    id: "FileReader-1",
    type: "FileReader",
    inputs: [],
    params: {
      url: "/home/..."
    }
  },
  {
    id: "LinearRegression-5",
    type: "LinearRegression",
    inputs: [
      ["FileReader-1", 0]
    ],
    params: {
      maxIter: 3,
      regParam: 5
    }
  }
];
