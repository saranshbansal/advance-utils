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
    return filteredObj[0] ? returnProperty ? filteredObj[0][returnProperty] : filteredObj[0] : null;
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
export const pushIntoManualMCPsArray = (arr, str, id, pre) => {
    const objIndex = (arr && arr.length > 0) ? arr.findIndex((obj => obj.id === pre + id)) : -1;
    if (objIndex > -1) {
        arr[objIndex].name = str;
    } else {
        arr.push({ id: pre + id, name: str });
    }
    return arr || [];
}

export const joinArray = (arr1, arr2) => {
    if (!arr1) {
        return arr2 || [];
    }
    if (!arr2) {
        return arr1 || [];
    }
    return arr1.reduceRight((prev, curr) => {
        prev.unshift(curr);
        return prev;
    }, arr2);
}

