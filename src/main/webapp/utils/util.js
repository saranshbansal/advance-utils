/* Commonly used reusable utility functions seperated out of the core code */

// sort simple or any deeply nested array by any property without causing mutations
export const sortBy = (arr, property) => {
    let copy = JSON.parse(JSON.stringify(arr));
    if (property) {
        return copy.sort((a, b) => (a[property] < b[property]) ? -1 : (a[property] > b[property]) ? 1 : 0);
    } else {
        return copy.sort();
    }
}

// get specific property list back from array of objects
export const findBy = (arr, property, checkAgainstValue, returnProperty) => {
    let filteredObj = arr ? arr.filter(el => el[property] === checkAgainstValue) : null;
    return filteredObj[0] ? filteredObj[0][returnProperty] : null;
}

// get object back from list by any property
export const lookUpBy = (value, arr, property) => {
    let filteredObj = arr ? arr.filter(el => el[property] === value) : null;
    return filteredObj[0] || null;
}

// convert delimiter seperated string to array
export const formatStringToObjArr = (str, delim, pre) => {
    const array = str.split(delim);
    const arrayOfObj = array.map((s, i) => { return { id: "other", name: pre + i + '-' + s } });
    return arrayOfObj || [];
}

// convert delimiter seperated string to array
export const formatStringToArr = (str, delim, pre) => {
    const array = str.split(delim);
    const encodedArray = array.map((s, i) => pre + i + '-' + s);
    return encodedArray || [];
}

// push value to array as an object mapped by given key/id
export const pushIntoArray = (arr, str, id) => {
    const objIndex = (arr && arr.length > 0) ? arr.findIndex((obj => obj.id === id)) : -1;
    if (objIndex > -1) {
        arr[objIndex].name = str;
    } else {
        arr.push({ id: id, name: str });
    }
    return arr || [];
}
