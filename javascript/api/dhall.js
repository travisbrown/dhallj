goog.module('dhall.js');

var DhallJs = goog.require('org.dhallj.js.DhallJs');

/**
 * @param {string} input
 * @return {null|string}
 */
function parse(input) {
  return DhallJs.parse(input);
}

/**
 * @param {string} input
 * @return {null|string}
 */
function typeCheck(input) {
  return DhallJs.typeCheck(input);
}

/**
 * @param {string} input
 * @return {null|string}
 */
function normalize(input) {
  return DhallJs.normalize(input);
}

// Otherwise we seem to lose stuff with some configurations?
parse("1");
typeCheck("1");
normalize("1");

goog.exportSymbol("parse", parse);
goog.exportSymbol("typeCheck", typeCheck);
goog.exportSymbol("normalize", normalize);